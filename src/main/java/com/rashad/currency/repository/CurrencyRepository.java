package com.rashad.currency.repository;

import com.rashad.currency.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    boolean existsByDate(LocalDate date);

    Optional<Currency> findByCodeAndDate(String code, LocalDate date);

    List<Currency> findByDate(LocalDate date);

    List<Currency> findByCodeOrderByDate(String code);
}
