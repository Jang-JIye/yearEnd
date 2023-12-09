package com.future.yearend.memo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByMonth(String month);


    List<Memo> findAllByMonthAndDay(String month, String day);
}
