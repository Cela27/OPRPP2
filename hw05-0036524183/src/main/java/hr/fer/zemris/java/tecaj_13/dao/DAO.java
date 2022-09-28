package hr.fer.zemris.java.tecaj_13.dao;


import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

public interface DAO {

	public List<BlogEntry> getBlogEntries(Long blogUserId) throws DAOException;
	
	public BlogEntry createBlogEntry(String title, String text, Long blogUserId);
	
	public boolean createBlogUser(String firstName, String lastName, String email, String nick, String password);
	
	public List<BlogUser> getBlogUsers();
	
	public BlogUser getBlogUser(String nick, String pass);
	
	public BlogUser getBlogUser(String nick);
	
	public BlogEntry getBlogEntry(Long entryId, Long blogUserId);
	
	public BlogEntry editBlogEntry(String title, String text, Long blogUserId, Long entryId);
	
	public BlogComment createBlogComment(String usersEmail, String message, Long entryId, Long userID);
}