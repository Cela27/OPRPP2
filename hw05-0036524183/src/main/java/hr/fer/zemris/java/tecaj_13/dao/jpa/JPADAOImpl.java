package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

public class JPADAOImpl implements DAO {

	@Override
	public boolean createBlogUser(String firstName, String lastName, String email, String nick, String password) {
		EntityManager em = JPAEMProvider.getEntityManager();
		boolean created = true;

		BlogUser blogUser = new BlogUser();

		blogUser.setFirstName(firstName);
		blogUser.setLastName(lastName);
		blogUser.setEmail(email);
		blogUser.setNick(nick);
		blogUser.setPasswordHash(hash(password));

		em.persist(blogUser);

		try {
			em.getTransaction().commit();

		} catch (EntityExistsException e) {
			created = false;
			e.printStackTrace();
		}

		return created;
	}

	@Override
	public List<BlogUser> getBlogUsers() {
		EntityManager em = JPAEMProvider.getEntityManager();
		List<BlogUser> blogUsers = null;
		try {
			Query q = em.createQuery("select user from BlogUser user");
			blogUsers = q.getResultList();
			return blogUsers;

		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
		}

		return blogUsers;
	}

	@Override
	public BlogUser getBlogUser(String nick, String pass) {
		EntityManager em = JPAEMProvider.getEntityManager();
		BlogUser blogUser = null;
		try {
			List<BlogUser> results = em.createQuery("SELECT user FROM BlogUser user where user.nick = :nick1")
					.setParameter("nick1", nick).getResultList();

			blogUser = results.get(0);
			boolean logged = false;
			String hash = blogUser.getPasswordHash();

			if (hash.equals(hash(pass)))
				logged = true;

			if (!logged)
				return null;

		} catch (NoResultException e) {
			// if there is no result
			e.printStackTrace();
		} catch (NonUniqueResultException e) {
			// if more than one result
			e.printStackTrace();
		}

		return blogUser;
	}

	@Override
	public BlogUser getBlogUser(String nick) {
		EntityManager em = JPAEMProvider.getEntityManager();
		BlogUser blogUser = null;
		try {
			List<BlogUser> results = em.createQuery("SELECT user FROM BlogUser user where user.nick = :nick1")
					.setParameter("nick1", nick).getResultList();

			blogUser = results.get(0);
			
		} catch (NoResultException e) {
			// if there is no result
			e.printStackTrace();
		} catch (NonUniqueResultException e) {
			// if more than one result
			e.printStackTrace();
		}

		return blogUser;
	}

	private String hash(String pass) {
		String generatedPassword = null;

		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// Add password bytes to digest
			md.update(pass.getBytes());

			// Get the hash's bytes
			byte[] bytes = md.digest();

			// This bytes[] has bytes in decimal format. Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	@Override
	public List<BlogEntry> getBlogEntries(Long blogUserId) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		List<BlogEntry> blogEntries = null;

		BlogUser blogUser = findById(blogUserId);

		blogEntries = blogUser.getEntries();

		return blogEntries;
	}

	@Override
	public BlogEntry createBlogEntry(String title, String text, Long blogUserId) {
		EntityManager em = JPAEMProvider.getEntityManager();
		BlogUser blogUser = null;

		List<BlogUser> blogUsers = null;
		try {
			blogUsers = getBlogUsers();
			for (BlogUser user : blogUsers) {
				if (user.getId() == blogUserId) {
					blogUser = user;
					break;
				}
			}
		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
		}
		if (blogUser == null)
			return null;

		BlogEntry blogEntry = new BlogEntry();
		blogEntry.setCreator(blogUser);
		blogEntry.setCreatedAt(new Date());
		blogEntry.setLastModifiedAt(blogEntry.getCreatedAt());
		blogEntry.setTitle(title);
		blogEntry.setText(text);

		em.persist(blogEntry);

		em.getTransaction().commit();

		blogUser.getEntries().add(blogEntry);

		em.persist(blogUser);

		return blogEntry;
	}

	public BlogUser findById(Long Id) {
		EntityManager em = JPAEMProvider.getEntityManager();
		List<BlogUser> blogUsers = null;
		try {
			blogUsers = getBlogUsers();

			for (BlogUser user : blogUsers) {
				if (user.getId() == Id)
					return user;
			}

		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BlogEntry getBlogEntry(Long entryId, Long blogUserId) {
		List<BlogEntry> blogEntries = getBlogEntries(blogUserId);
		if(blogEntries.size()==0)
			return null;
		for (BlogEntry entry : blogEntries) {
			if (entry.getId() == entryId) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BlogEntry editBlogEntry(String title, String text, Long blogUserId, Long entryId) {
		EntityManager em = JPAEMProvider.getEntityManager();
		BlogUser blogUser = null;

		List<BlogUser> blogUsers = null;
		try {
			blogUsers = getBlogUsers();
			for (BlogUser user : blogUsers) {
				if (user.getId() == blogUserId) {
					blogUser = user;
					break;
				}
			}
		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
		}
		if (blogUser == null)
			return null;

		BlogEntry blogEntry = getBlogEntry(entryId, blogUserId);
		blogEntry.setTitle(title);
		blogEntry.setText(text);
		em.persist(blogEntry);

		em.getTransaction().commit();
		// tu mozda treba promjenit listu blogEntrya sa izmjenjenima la nisam sig
//		blogUser.getEntries().add(blogEntry);
//		
//		em.persist(blogUser);

		return blogEntry;
	}

	@Override
	public BlogComment createBlogComment(String usersEmail, String message, Long entryId, Long userId) {
		EntityManager em = JPAEMProvider.getEntityManager();
		BlogComment blogComment = new BlogComment();
		blogComment.setMessage(message);
		if (usersEmail == "null" || usersEmail=="" || usersEmail==null) {
			blogComment.setUsersEMail("Anoniman");
		}else {
			blogComment.setUsersEMail(usersEmail);
		}
			
		
		blogComment.setPostedOn(new Date());
		
		BlogEntry blogEntry = getBlogEntry(entryId, userId);
		blogComment.setBlogEntry(blogEntry);
		
		em.persist(blogComment);

		em.getTransaction().commit();

		blogEntry.getComments().add(blogComment);

		em.persist(blogEntry);

		return blogComment;
	}

}