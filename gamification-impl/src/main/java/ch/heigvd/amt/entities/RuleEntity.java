package ch.heigvd.amt.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * REFER TO SPEC THIS IS A NIGHTMARE CLASS
 */

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
public class RuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "fk_apikey")
    private ApiKeyEntity apiKeyEntity;

    @OneToOne(cascade = {CascadeType.ALL})
    private If _if;
    @OneToOne(cascade = {CascadeType.ALL})
    private Then _then;

    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Data
    @Builder
    public static class If {
        @Id
        private String type;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Data
    @Builder
    public static class Then {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        private String awardBadge;
        @OneToOne(cascade = {CascadeType.ALL})
        private AwardPoints awardPoints;

        @AllArgsConstructor
        @NoArgsConstructor
        @Entity
        @Data
        @Builder
        public static class AwardPoints {

            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private long id;

            private int amount;
            private String pointScale;

        }
    }

}
