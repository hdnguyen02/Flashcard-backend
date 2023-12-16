package hdnguyen.component;


import hdnguyen.common.CardType;
import hdnguyen.entity.Card;
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

    public List<Card> getCardsToStudy(int idDeck, int newLimit, int reviewLimit){

        String strQueryNewCard = "SELECT c FROM Card c WHERE c.deck.id = :idDeck AND c.type = :type";
        TypedQuery<Card> queryNewCard = entityManager.createQuery(strQueryNewCard, Card.class);
        queryNewCard.setParameter("idDeck", idDeck);
        queryNewCard.setParameter("type", String.valueOf(CardType.FRESH));
        queryNewCard.setFirstResult(0);
        queryNewCard.setMaxResults(newLimit);

        String strQueryReviewCard = "SELECT c FROM Card c WHERE c.deck.id = :idDeck AND c.type = :type AND c.due <= CURRENT_DATE";
        TypedQuery<Card> queryReviewCard = entityManager.createQuery(strQueryReviewCard, Card.class);
        queryReviewCard.setParameter("idDeck", idDeck);
        queryReviewCard.setParameter("type", String.valueOf(CardType.REVIEW));
        queryReviewCard.setFirstResult(0);
        queryReviewCard.setMaxResults(reviewLimit);

        String strQueryLearningCard = "SELECT c FROM Card c WHERE c.deck.id = :idDeck AND c.type = :type";
        TypedQuery<Card> queryLearningCard = entityManager.createQuery(strQueryLearningCard, Card.class);
        queryLearningCard.setParameter("idDeck", idDeck);
        queryLearningCard.setParameter("type", String.valueOf(CardType.LEARNING));

        List<Card> newCards = queryNewCard.getResultList();
        List<Card> reviewCards = queryReviewCard.getResultList();
        List<Card> learningCards  = queryLearningCard.getResultList();

        List<Card> cards = new ArrayList<>();
        cards.addAll(learningCards);
        cards.addAll(reviewCards);
        cards.addAll(newCards);

        return cards;
    }
}
