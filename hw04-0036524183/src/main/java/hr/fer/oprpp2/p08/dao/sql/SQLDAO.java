package hr.fer.oprpp2.p08.dao.sql;

import hr.fer.oprpp2.p08.dao.DAO;
import hr.fer.oprpp2.p08.dao.DAOException;
import hr.fer.oprpp2.p08.model.UnosPollOptions;
import hr.fer.oprpp2.p08.model.UnosPolls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter 
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *  
 * @author marcupic
 */
public class SQLDAO implements DAO {

	
	@Override
	public List<UnosPolls> dohvatiUnoseIzPolls() throws DAOException {
		List<UnosPolls> unosi = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, title, message from Polls order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						UnosPolls unos = new UnosPolls();
						unos.setId(rs.getLong(1));
						unos.setTitle(rs.getString(2));
						unos.setMessage(rs.getString(3));
						unosi.add(unos);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}
		return unosi;
	}

	@Override
	public List<UnosPollOptions> dohvatiUnoseIzPollOptions() throws DAOException {
		List<UnosPollOptions> unosi = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, optionTitle, optionLink, pollID, votesCount from PollOptions order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						UnosPollOptions unos = new UnosPollOptions();
						unos.setId(rs.getLong(1));
						unos.setOptionTitle(rs.getString(2));
						unos.setOptionLink(rs.getString(3));
						unos.setPollID(rs.getInt(4));
						unos.setGlasovi(rs.getInt(5));
						unosi.add(unos);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}
		return unosi;
	}

	@Override
	public UnosPolls dohvatiUnosIzPolls(long id) throws DAOException {
		UnosPolls unos = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, title, message from Polls where id=?");
			pst.setLong(1, Long.valueOf(id));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if(rs!=null && rs.next()) {
						unos = new UnosPolls();
						unos.setId(rs.getLong(1));
						unos.setTitle(rs.getString(2));
						unos.setMessage(rs.getString(3));
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata korisnika.", ex);
		}
		return unos;
	}

	@Override
	public UnosPollOptions dohvatiUnosIzPollOptions(long id) throws DAOException {
		UnosPollOptions unos = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, optionTitle, optionLink, pollID, votesCount from PollOptions where id=?");
			pst.setLong(1, Long.valueOf(id));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if(rs!=null && rs.next()) {
						unos = new UnosPollOptions();
						unos.setId(rs.getLong(1));
						unos.setOptionTitle(rs.getString(2));
						unos.setOptionLink(rs.getString(3));
						unos.setPollID(rs.getInt(4));
						unos.setGlasovi(rs.getInt(5));
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata korisnika.", ex);
		}
		return unos;
	}

	@Override
	public List<UnosPollOptions> dohvatiUnoseIzPollOptionsZaOdređenPoll(Integer pollID) throws DAOException {
		List<UnosPollOptions> unosi = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, optionTitle, optionLink, pollID, votesCount from PollOptions where pollID=?");
			pst.setLong(1, pollID);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						UnosPollOptions unos = new UnosPollOptions();
						unos.setId(rs.getLong(1));
						unos.setOptionTitle(rs.getString(2));
						unos.setOptionLink(rs.getString(3));
						unos.setPollID(rs.getInt(4));
						unos.setGlasovi(rs.getInt(5));
						unosi.add(unos);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}
		return unosi;
	}

	@Override
	public void izmjeniGlas(int id, int newVotesCount) {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("UPDATE PollOptions set votesCount=? WHERE id=?");
			pst.setInt(1, newVotesCount);
			pst.setLong(2, id);
			try {
				pst.executeUpdate();
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}
		
	}

	

}