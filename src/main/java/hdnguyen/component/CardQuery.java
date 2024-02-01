package hdnguyen.component;


import hdnguyen.common.CardType;
import hdnguyen.entity.ECard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CardQuery {

    @PersistenceContext
    private final EntityManager entityManager;

    public List<ECard> getCardsToStudy(String idDeck, int newLimit, int reviewLimit){

        String strQueryNewCard = "SELECT c FROM Card c WHERE c.deck.id = :idDeck AND c.type = :type";
        TypedQuery<ECard> queryNewCard = entityManager.createQuery(strQueryNewCard, ECard.class);
        queryNewCard.setParameter("idDeck", idDeck);
        queryNewCard.setParameter("type", String.valueOf(CardType.FRESH));
        queryNewCard.setFirstResult(0);
        queryNewCard.setMaxResults(newLimit);

        String strQueryReviewCard = "SELECT c FROM Card c WHERE c.deck.id = :idDeck AND c.type = :type AND c.due <= CURRENT_DATE";
        TypedQuery<ECard> queryReviewCard = entityManager.createQuery(strQueryReviewCard, ECard.class);
        queryReviewCard.setParameter("idDeck", idDeck);
        queryReviewCard.setParameter("type", String.valueOf(CardType.REVIEW));
        queryReviewCard.setFirstResult(0);
        queryReviewCard.setMaxResults(reviewLimit);

        String strQueryLearningCard = "SELECT c FROM Card c WHERE c.deck.id = :idDeck AND c.type = :type";
        TypedQuery<ECard> queryLearningCard = entityManager.createQuery(strQueryLearningCard, ECard.class);
        queryLearningCard.setParameter("idDeck", idDeck);
        queryLearningCard.setParameter("type", String.valueOf(CardType.LEARNING));

        List<ECard> newCards = queryNewCard.getResultList();
        List<ECard> reviewCards = queryReviewCard.getResultList();
        List<ECard> learningCards  = queryLearningCard.getResultList();

        List<ECard> cards = new ArrayList<>();
        cards.addAll(learningCards);
        cards.addAll(reviewCards);
        cards.addAll(newCards);

        return cards;
    }
}
