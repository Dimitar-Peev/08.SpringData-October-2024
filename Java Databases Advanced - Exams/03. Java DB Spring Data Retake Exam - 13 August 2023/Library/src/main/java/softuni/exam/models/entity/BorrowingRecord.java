package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "borrowing_records")
public class BorrowingRecord extends BaseEntity {

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Column
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private LibraryMember member;

    @Override
    public String toString() {
        return String.format("Book title: %s%n" +
                        "*Book author: %s%n" +
                        "**Date borrowed: %s%n" +
                        "***Borrowed by: %s %s%n",
                getBook().getTitle(), getBook().getAuthor(), getBorrowDate(),
                getMember().getFirstName(), getMember().getLastName());
    }
}
