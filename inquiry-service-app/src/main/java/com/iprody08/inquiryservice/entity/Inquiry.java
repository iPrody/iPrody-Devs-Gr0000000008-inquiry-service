package com.iprody08.inquiryservice.entity;

import com.iprody08.inquiryservice.entity.enums.InquiryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "inquiries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = true)
    private Source source;

    @Column(nullable = true, columnDefinition = "text")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InquiryStatus status;

    @Column(nullable = true, columnDefinition = "text")
    private String note;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Long productRefId;

    @Column(nullable = false)
    private Long customerRefId;

    @Column(nullable = false)
    private Long managerRefId;

    @PrePersist
    public void prePersist() {
        if (productRefId == null) {
            productRefId = 0L;
        }
        if (customerRefId == null) {
            customerRefId = 0L;
        }
        if (managerRefId == null) {
            managerRefId = 0L;
        }
    }

    public static Inquiry createNewInquiry(Long id, String comment, InquiryStatus status, String note, Source source) {
        Inquiry inquiry = new Inquiry();
        inquiry.setId(id);
        inquiry.setComment(comment);
        inquiry.setStatus(status);
        inquiry.setNote(note);
        inquiry.setSource(source);
        return inquiry;
    }
}
