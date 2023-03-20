package com.rashad.currency.controller;

import com.rashad.currency.entity.Currency;
import com.rashad.currency.model.CustomResponse;
import com.rashad.currency.service.impl.CurrencyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
@SecurityRequirement(name = "BearerJwt")
@Tag(name = "2. Currency Controller")
public class CurrencyController {

    private final CurrencyServiceImpl currencyService;

    @Operation(
            summary = "Seçilmiş tarix üçün məzənnələrin verilənlər bazasında saxlanılması",
            description = "Bu api-dən istifadə etmək üçün ilk öncə Login olduqdan sonra sizə təqdim " +
                    "olunan accessToken-ni sağda qıfıl ikonun üstünə vurduqda açılan pəncərədə Value xanasına " +
                    "daxil edib Authorize düyməsini basın. Artıq siz bu api-dən istifadə edə bilərsiz. " +
                    "Seçilmiş tarix üçün məzənnələrin Azərbaycan Respublikası Mərkəzi Bankının məzənnə " +
                    "bülletenindən yüklənərək verilənlər bazasında saxlanılması üçün asagıdakı " +
                    "Try it out düyməsini vurub tarixi uyğun xanaya daxil edib Execute düyməsini basın.",
            parameters = {@Parameter(name = "date", description = "tarix", example = "10.10.2010")},
            responses = {@ApiResponse(responseCode = "201", description = "Success Response",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))}
    )
    @PostMapping("/fetch/{date}")
    public ResponseEntity<?> fetchCurrencyFromCBAR(@PathVariable String date)
            throws ParserConfigurationException, IOException, SAXException {
        String message = currencyService.fetchCurrencyFromCBAR(date);
        return new ResponseEntity<>(new CustomResponse(message), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Seçilmiş tarix üçün yadda saxlanılmış məzənnələrin silinməsi",
            description = "Bu api-dən istifadə etmək üçün ilk öncə Login olduqdan sonra sizə təqdim " +
                    "olunan accessToken-ni sağda qıfıl ikonun üstünə vurduqda açılan pəncərədə Value xanasına " +
                    "daxil edib Authorize düyməsini basın. Artıq siz bu api-dən istifadə edə bilərsiz. " +
                    "Seçilmiş tarix üçün yadda saxlanılmış məzənnələrin silinməsi üçün asagıdakı " +
                    "Try it out düyməsini vurub tarixi uyğun xanaya daxil edib Execute düyməsini basın.",
            parameters = {@Parameter(name = "date", description = "tarix", example = "10.10.2010")},
            responses = {@ApiResponse(responseCode = "200", description = "Success Response",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))}
    )
    @DeleteMapping("/delete/{date}")
    public ResponseEntity<?> deleteCurrencyByDate(@PathVariable String date) {
        String message = currencyService.deleteFromDB(date);
        return new ResponseEntity<>(new CustomResponse(message), HttpStatus.OK);
    }

    @Operation(
            summary = "Seçilmiş tarixdə və seçilmiş valyutaya qarşı məzənnənin əldə olunması",
            description = "Bu api-dən istifadə etmək üçün ilk öncə Login olduqdan sonra sizə təqdim " +
                    "olunan accessToken-ni sağda qıfıl ikonun üstünə vurduqda açılan pəncərədə Value xanasına " +
                    "daxil edib Authorize düyməsini basın. Artıq siz bu api-dən istifadə edə bilərsiz. " +
                    "Azərbaycan manatının seçilmiş tarixdə və seçilmiş valyutaya qarşı " +
                    "məzənnəsinin əldə olunması üçün asagıdakı Try it out düyməsini vurub " +
                    "tarixi və valyuta kodunu uyğun xanalara daxil edib Execute düyməsini basın.",
            parameters = {@Parameter(name = "code", description = "kod", example = "USD"),
                    @Parameter(name = "date", description = "tarix", example = "10.10.2010")},
            responses = {@ApiResponse(responseCode = "200", description = "Success Response",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))}
    )
    @GetMapping("/{code}/{date}")
    public ResponseEntity<?> getCurrencyByCodeAndDate(@PathVariable String code,
                                                      @PathVariable String date) {
        Currency data = currencyService.getCurrencyByCodeAndDate(code, date);
        if (data.getCode() == null) {
            String message = date + " tarixi və " + code + " valyuta kodu üçüm məzənnə bazada mövcud deyil";
            return new ResponseEntity<>(new CustomResponse(false, message), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new CustomResponse(data), HttpStatus.OK);

    }

    @Operation(
            summary = "Seçilmiş tarixdə bütün xarici valyutalara qarşı məzənnələrin əldə olunması",
            description = "Bu api-dən istifadə etmək üçün ilk öncə Login olduqdan sonra sizə təqdim " +
                    "olunan accessToken-ni sağda qıfıl ikonun üstünə vurduqda açılan pəncərədə Value xanasına " +
                    "daxil edib Authorize düyməsini basın. Artıq siz bu api-dən istifadə edə bilərsiz. " +
                    "Azərbaycan manatının seçilmiş tarixdə bütün xarici valyutalara qarşı məzənnələrinin " +
                    "əldə olunması üçün asagıdakı Try it out düyməsini vurub tarixi uyğun xanaya daxil " +
                    "edib Execute düyməsini basın.",
            parameters = {@Parameter(name = "date", description = "tarix", example = "10.10.2010")},
            responses = {@ApiResponse(responseCode = "200", description = "Success Response",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))}
    )
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getAllCurrencyByDate(@PathVariable String date) {
        List<Currency> data = currencyService.getAllCurrencyByDate(date);
        if (data.isEmpty()) {
            String message = date + " tarixi üçüm məzənnələr bazada mövcud deyil";
            return new ResponseEntity<>(new CustomResponse(false, message), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new CustomResponse(data), HttpStatus.OK);
    }

    @Operation(
            summary = "Seçilmiş xarici valyutaya qarşı bütün tarixlərdəki məzənnələrinin əldə olunması",
            description = "Bu api-dən istifadə etmək üçün ilk öncə Login olduqdan sonra sizə təqdim " +
                    "olunan accessToken-ni sağda qıfıl ikonun üstünə vurduqda açılan pəncərədə Value xanasına " +
                    "daxil edib Authorize düyməsini basın. Artıq siz bu api-dən istifadə edə bilərsiz. " +
                    "Azərbaycan manatının seçilmiş xarici valyutaya qarşı bütün tarixlərdəki " +
                    "məzənnələrinin əldə olunması üçün asagıdakı Try it out düyməsini vurub valyuta kodunu " +
                    "uyğun xanaya daxil edib Execute düyməsini basın.",
            parameters = {@Parameter(name = "code", description = "kod", example = "USD")},
            responses = {@ApiResponse(responseCode = "200", description = "Success Response",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))}
    )
    @GetMapping("/code/{code}")
    public ResponseEntity<?> getAllCurrencyByCode(@PathVariable String code) {
        List<Currency> data = currencyService.getAllCurrencyByCode(code);
        if (data.isEmpty()) {
            String message = code + " valyuta kodu üçüm məzənnələr bazada mövcud deyil";
            return new ResponseEntity<>(new CustomResponse(false, message), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new CustomResponse(data), HttpStatus.OK);
    }
}
