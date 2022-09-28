package hr.fer.oprpp2.p08.dao;

import java.util.List;

import hr.fer.oprpp2.p08.model.UnosPollOptions;
import hr.fer.oprpp2.p08.model.UnosPolls;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	/**
	 * Dohvaća sve unose u bazi Polls;
	 * 
	 * @return listu unosa
	 * @throws DAOException u slučaju pogreške
	 */
	public List<UnosPolls> dohvatiUnoseIzPolls() throws DAOException;
	
	/**
	 * Dohvaća sve unose za PollOptions.
	 * 
	 * @return
	 * @throws DAOException
	 */
	public List<UnosPollOptions> dohvatiUnoseIzPollOptions() throws DAOException;
	
	/**
	 * Dohvaća sve unose u bazi Polls za zadani id;
	 * @param id
	 * @return listu unosa
	 * @throws DAOException u slučaju pogreške
	 */
	public UnosPolls dohvatiUnosIzPolls(long id) throws DAOException;
	
	/**
	 * Dohvaća sve unose za PollOptions za zadani id.
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public UnosPollOptions dohvatiUnosIzPollOptions(long id) throws DAOException;
	
	/**
	 * Dohvaća sve unose za PollOptions za zadani pollID.
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public List<UnosPollOptions> dohvatiUnoseIzPollOptionsZaOdređenPoll(Integer pollID) throws DAOException;
	
	/**
	 * Mjenja broj glasova za unos i danim ID-em.;
	 * @param pollID
	 * @param id
	 */
	public void izmjeniGlas(int id, int newVotesCount);
	
}