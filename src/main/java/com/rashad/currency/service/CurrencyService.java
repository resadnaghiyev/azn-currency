package com.rashad.currency.service;

import com.rashad.currency.entity.Currency;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CurrencyService {

    String fetchCurrencyFromCBAR(String date) throws ParserConfigurationException, IOException, SAXException;

    String deleteFromDB(String date);

    List<Currency> getAllCurrencyByCode(String code);

    List<Currency> getAllCurrencyByDate(String date);

    Currency getCurrencyByCodeAndDate(String code, String date) throws ParseException;
}
