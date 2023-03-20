package com.rashad.currency.service;

import com.rashad.currency.entity.Currency;
import com.rashad.currency.repository.CurrencyRepository;
import com.rashad.currency.service.impl.CurrencyServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    private Currency currency;

    @BeforeEach
    public void init() {
        currency = Currency.builder()
                .id(1L)
                .date(LocalDate.now())
                .type("Xarici Valyuta")
                .code("USD")
                .nominal("1")
                .name("1 ABŞ dolları")
                .value("1.7")
                .build();
    }

    @Test
    public void CurrencyService_FetchAndSaveCurrency_Success_ReturnsString()
            throws ParserConfigurationException, IOException, SAXException {
        when(restTemplate.getForEntity("https://www.cbar.az/currencies/10.10.2010.xml", String.class))
                .thenReturn(new ResponseEntity<>("<ValCurs Date=\"08.10.2010\">\n" +
                        "   <ValType Type=\"Xarici valyutalar\">\n" +
                        "      <Valute Code=\"USD\">\n" +
                        "         <Nominal>1</Nominal>\n" +
                        "         <Name>1 ABŞ dolları</Name>\n" +
                        "         <Value>0.8021</Value>\n" +
                        "      </Valute>\n" +
                        "   </ValType>\n" +
                        "</ValCurs>", HttpStatus.OK));
        when(currencyRepository.existsByDate(Mockito.any(LocalDate.class))).thenReturn(false);
        when(currencyRepository.save(Mockito.any(Currency.class))).thenReturn(currency);
        String result = currencyService.fetchCurrencyFromCBAR("10.10.2010");
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.endsWith("olundu.")).isTrue();
    }

    @Test
    public void CurrencyService_FetchAndSaveCurrency_Failed_ReturnsString()
            throws ParserConfigurationException, IOException, SAXException {
        when(currencyRepository.existsByDate(Mockito.any(LocalDate.class))).thenReturn(true);
        String result = currencyService.fetchCurrencyFromCBAR("10.10.2010");
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.endsWith("mövcuddur!")).isTrue();
    }


    @Test
    public void CurrencyService_DeleteCurrency_Success_ReturnsString() {
        when(currencyRepository.findByDate(Mockito.any(LocalDate.class)))
                .thenReturn(List.of(currency, currency));
        String result = currencyService.deleteFromDB("10.10.2010");
        Assertions.assertThat(result.endsWith("silindi")).isTrue();
    }

    @Test
    public void CurrencyService_DeleteCurrency_Failed_ReturnsString() {
        when(currencyRepository.findByDate(Mockito.any(LocalDate.class)))
                .thenReturn(List.of());
        String result = currencyService.deleteFromDB("10.10.2010");
        Assertions.assertThat(result.endsWith("deyil")).isTrue();
    }

    @Test
    public void CurrencyService_GetCurrencyByCode_Success_ReturnsCurrencyList() {
        when(currencyRepository.findByCodeOrderByDate("USD"))
                .thenReturn(List.of(currency, currency));
        List<Currency> currencyList = currencyService.getAllCurrencyByCode("USD");
        Assertions.assertThat(currencyList.size()).isEqualTo(2);
        Assertions.assertThat(currencyList.get(0).getCode()).isEqualTo("USD");
    }

    @Test
    public void CurrencyService_GetCurrencyByCode_Failed_ReturnsCurrencyList() {
        when(currencyRepository.findByCodeOrderByDate("USD")).thenReturn(List.of());
        List<Currency> currencyList = currencyService.getAllCurrencyByCode("USD");
        Assertions.assertThat(currencyList.size()).isEqualTo(0);
        Assertions.assertThat(currencyList).isEmpty();
    }

    @Test
    public void CurrencyService_GetCurrencyByDate_Success_ReturnsCurrencyList() {
        when(currencyRepository.findByDate(Mockito.any(LocalDate.class)))
                .thenReturn(List.of(currency, currency));
        List<Currency> currencyList = currencyService.getAllCurrencyByDate("10.10.2010");
        Assertions.assertThat(currencyList.size()).isEqualTo(2);
        Assertions.assertThat(currencyList.get(0).getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    public void CurrencyService_GetCurrencyByDate_Failed_ReturnsCurrencyList() {
        when(currencyRepository.findByCodeOrderByDate("10.10.2010")).thenReturn(List.of());
        List<Currency> currencyList = currencyService.getAllCurrencyByCode("10.10.2010");
        Assertions.assertThat(currencyList.size()).isEqualTo(0);
        Assertions.assertThat(currencyList).isEmpty();
    }

    @Test
    public void CurrencyService_GetCurrencyByCodeAndDate_Success_ReturnsCurrency() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dateTime = LocalDate.parse("10.10.2010", formatter);
        Optional<Currency> optionalCurrency = Optional.ofNullable(currency);
        when(currencyRepository.findByCodeAndDate("USD", dateTime))
                .thenReturn(optionalCurrency);
        Currency currency = currencyService.getCurrencyByCodeAndDate("USD", "10.10.2010");
        Assertions.assertThat(currency).isNotNull();
        Assertions.assertThat(currency.getCode()).isEqualTo("USD");
    }

    @Test
    public void CurrencyService_GetCurrencyByCodeAndDate_Failed_ReturnsCurrency() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dateTime = LocalDate.parse("10.10.2010", formatter);
        Optional<Currency> optionalCurrency = Optional.empty();
        when(currencyRepository.findByCodeAndDate("USD", dateTime))
                .thenReturn(optionalCurrency);
        Currency currency = currencyService.getCurrencyByCodeAndDate("USD", "10.10.2010");
        Assertions.assertThat(currency).isInstanceOf(currency.getClass());
        Assertions.assertThat(currency.getCode()).isNull();
    }
}
