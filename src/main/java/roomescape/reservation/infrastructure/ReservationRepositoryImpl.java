package roomescape.reservation.infrastructure;

import static roomescape.reservation.domain.QReservation.reservation;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepositoryCustom;

@Repository
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ReservationRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Reservation> searchByConditions(final LocalDate dateFrom, final LocalDate dateTo,
                                                final Long memberId,
                                                final Long themeId) {
        BooleanBuilder builder = buildCondition(dateFrom, dateTo, memberId, themeId);

        return jpaQueryFactory.selectFrom(reservation)
                .where(builder)
                .fetch();
    }

    private BooleanBuilder buildCondition(final LocalDate dateFrom, final LocalDate dateTo,
                                          final Long memberId,
                                          final Long themeId) {
        BooleanBuilder builder = new BooleanBuilder();
        if (dateFrom != null && dateTo != null) {
            builder.and(reservation.dateTime.date.goe(dateFrom));
            builder.and(reservation.dateTime.date.loe(dateTo));
        }
        if (memberId != null) {
            builder.and(reservation.member.id.eq(memberId));
        }
        if (themeId != null) {
            builder.and(reservation.theme.id.eq(themeId));
        }
        return builder;
    }
}
