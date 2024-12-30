package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.BorrowingRecord;
import softuni.exam.models.entity.enums.Genre;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    @Query("SELECT br FROM BorrowingRecord br " +
            "JOIN FETCH br.book b " +
            "JOIN FETCH br.member m " +
            "WHERE br.borrowDate < :date " +
            "AND b.genre = :genre " +
            "ORDER BY br.borrowDate DESC")
    List<BorrowingRecord> findAllByBorrowDateBeforeAndOrderByBorrowDateDesc(LocalDate date, Genre genre);
}
