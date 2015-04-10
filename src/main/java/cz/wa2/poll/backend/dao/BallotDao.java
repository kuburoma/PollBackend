package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.Ballot;
import cz.wa2.poll.backend.entities.EntitiesList;
import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.Expression;

public class BallotDao extends GenericDaoImpl<Ballot, Long> {

    public BallotDao() {
        super(Ballot.class);
    }

    /**
     * Nastavuje pouze novou hodnotu odpovedi.
     * Zbytek kvuli manipulovani s volbami zmenit nejde.
     *
     * @param ballot
     * @throws DaoException
     */
    @Override
    public void update(Ballot ballot) throws DaoException {
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            Ballot object = em.find(Ballot.class, ballot.getId());
            object.setAnswer(ballot.getAnswer());
            em.merge(object);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw new DaoException("Chyba při vytvoření entity", e);
        } finally {
            em.close();
        }
    }

    /**
     * Pro uzivatele a hlasovani najde jeho hlasovaci listek.
     *
     * @param voterId id uzivatele
     * @param pollId  id hlasovani
     * @return
     * @throws DaoException
     */
    public Ballot findBallot(Long voterId, Long pollId) throws DaoException {
        try {
            initializationSearch();

            Expression<Voter> voterBallot = root.get("voter");
            Expression<Poll> pollBallot = root.get("poll");

            Voter voter = em.find(Voter.class, voterId);
            Poll poll = em.find(Poll.class, pollId);

            cq.where(cb.and(cb.equal(voterBallot, voter), cb.equal(pollBallot, poll)));

            return em.createQuery(cq).getSingleResult();
        } catch (PersistenceException e) {
            throw new DaoException("Error when loading ballot", e);
        } finally {
            em.close();
        }
    }

    /**
     * Vyhledává pro hlasování ejho hlasovací lístky.
     *
     * @param id
     * @param offset
     * @param base
     * @param order
     * @return
     * @throws DaoException
     * @throws InputException
     */
    public EntitiesList<Ballot> findPollBallots(Long id, Integer offset, Integer base, String order) throws DaoException, InputException {
        try {
            initializationSearch();

            Expression<Poll> pollBallot = root.get("poll");
            Poll poll = em.find(Poll.class, id);
            cq.select(root).where(cb.equal(pollBallot, poll));

            return makeEntitiesList(offset, base, order);
        } catch (PersistenceException e) {
            throw new DaoException("Error when finding ballots", e);
        } finally {
            em.close();
        }
    }
}
