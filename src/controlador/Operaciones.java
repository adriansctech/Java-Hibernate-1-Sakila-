/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;


import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Actor;
import modelo.Film;
import modelo.FilmActor;
import modelo.Language;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author terrorist
 */



public class Operaciones {
    
    
    private final SessionFactory sessionFactory;

    
    //Constructor
    public Operaciones() {
        sessionFactory = NewHibernateUtil.getSessionFactory();
    }
    
    
    //////////////////////////////////////////////////////////////////////////// START ACTORS ACTIONS TO DO
    //List all the actors of actors table   
    public List<Actor> mostrarActores() {
        Session session = sessionFactory.openSession();
        Transaction tx;
        tx = session.beginTransaction();
        Query q = session.createQuery("from Actor");
        List<Actor> lista = q.list();
        Iterator<Actor> iter = lista.iterator();
        tx.commit();
        session.close();        
        return lista;
    }    
    
    public List<Actor> mostraridiomas() {
        Session session = sessionFactory.openSession();
        Transaction tx;
        tx = session.beginTransaction();
        Query q = session.createQuery("from Language");
        List<Actor> lista = q.list();
        Iterator<Actor> iter = lista.iterator();
        tx.commit();
        session.close();        
        return lista;
    }
    
    //Look for the Actor by ID
    public Actor wantActorByID(short id) {
        Session session = sessionFactory.openSession();
        Transaction tx;
        tx = session.beginTransaction();
        Actor actor = (Actor) session.load(Actor.class, id);
        System.out.println("ACTOR IS; "+actor);
        tx.commit();
        session.close();
        return actor;
    }
    
    //Insert new actor in table actors
    public void newActor(Actor actor) {
        Session session = sessionFactory.openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            session.save(actor);
            tx.commit();
            JOptionPane.showMessageDialog(null, "Insert new actor correctly.");
        }
        catch(ConstraintViolationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, " An error has ocurred ton insert new values. ");
        }
        finally {
            session.close();
        }
    }    
    //Modify actor information of table actors, for this i needed actor object, 
    // other information of this and very important the ID.
    public void updateActor(Actor actor, String name, String surname, Date date, short id) {
        Session session = sessionFactory.openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            //Create sentence
            Query query = session.createQuery("update  Actor set first_name = :NAME" + 
                                                ", last_name = :SURNAME" + 
                                                ", last_update = :DATE"+
                                                " where actor_id = :ID");
            //Insert Id value in query
            query.setParameter("NAME", name);
            query.setParameter("SURNAME", surname);
            query.setParameter("DATE", date);
            query.setParameter("ID", id);
            int result = query.executeUpdate(); 
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Update actor information correctly. ");
            }            
            tx.commit();  
        }
        catch(ConstraintViolationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, " An error has ocurred during the process ");
        }
        finally {
            session.close();
        }
    }
    //Delete an actor by id
    public void deleteActor(Actor actor, short id){
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {                                   
            tx = session.beginTransaction();   
            //Create sentence, for delete information of this actor from table film_actor if exist
            Query query1 = session.createQuery("delete from FilmActor where actor_id = :ID");
            query1.setParameter("ID", id);            
            int result1=query1.executeUpdate();
            //If result is distinct of 0, this actor have any film
            if(result1<0){
                JOptionPane.showMessageDialog(null, "This actor have any film. Delete correctly.");
            }           
            //Create sentence for delete actyor from table actor
            Query query = session.createQuery("delete from Actor where actor_id = :ID");
            //Insert Id value in query
            query.setParameter("ID", id);
            int result2 = query.executeUpdate(); 
            //If result is more bigger than 0, this actor have any film
            if (result2 > 0) {
                JOptionPane.showMessageDialog(null, "Delete  actor information correctly delete. ");
            }     
            
            tx.commit();            
        }
        catch(ConstraintViolationException e) {
            e.printStackTrace();    
            System.out.println("ERROR "+e);            
        }
        finally {
            session.close();
        }
       
    }
    //////////////////////////////////////////////////////////////////////////// FINISH ACTORS ACTIONS TO DO
    
    
    
    //////////////////////////////////////////////////////////////////////////// START FILMS ACTIONS TO DO
    //List all the Films of films table  
    
    public List<Film> showFilms() {
        Session session = sessionFactory.openSession();
        Transaction tx;
        tx = session.beginTransaction();
        Query q = session.createQuery("from Film");
        List<Film> lista = q.list();
        tx.commit();
        session.close();
        return lista;
    }
    
    
    
    
    
    //INSERT a new Film on table film
    public void newFilm(Film film){
        // This method do 2 actions, first save in DB a filmObject using the first constructor,
        // and second action update information of this films for inlcude more information.
        Session session = sessionFactory.openSession();
        Transaction tx;        
        try{
            tx = session.beginTransaction();
            session.save(film);
            tx.commit();                        
            tx = session.beginTransaction();
            Query query = session.createQuery("update Film set"                                                
                                                + " description = :DESCRIPTION"
                                                + ", length = :LENGTH"
                                                + " where filmId = :ID"); 
            query.setParameter("DESCRIPTION", film.getDescription());
            query.setParameter("LENGTH", film.getLength());
            query.setParameter("ID", film.getFilmId());            
            int result = query.executeUpdate(); 
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Insert a new Film correctly");
            }            
            tx.commit();
            
            tx = session.beginTransaction();
            
        }
        catch(ConstraintViolationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, " An error has ocurred ton insert new values. ");
        }
        finally {
            session.close();
        }
    }
    
    
    //MODIFY a film existent information
    public void updateFilm(Film film){
        Session session = sessionFactory.openSession();
        Transaction tx;
        try{
            tx = session.beginTransaction();
            //Create sentence
            Query query = session.createQuery("update  Film set languageByLanguageId = :LANGUAGE" + 
                                                ", title = :TITLE" + 
                                                ", rentalDuration = :RENTALDURATION"+
                                                ", rentalRate = :RENTALRATE"+ 
                                                ", replacementCost = :REPLACEMENTCOST"+
                                                ", lastUpdate = :LASTUPDATE"+
                                                " where filmId = :ID");
            //Insert a diferent values in query
            query.setParameter("LANGUAGE", film.getLanguageByLanguageId());
            query.setParameter("TITLE", film.getTitle());
            query.setParameter("RENTALDURATION", film.getRentalDuration());
            query.setParameter("RENTALRATE", film.getRentalRate());
            query.setParameter("REPLACEMENTCOST", film.getReplacementCost());
            query.setParameter("LASTUPDATE", film.getLastUpdate());
            query.setParameter("ID", film.getFilmId());
            int result = query.executeUpdate(); 
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Update Film information correctly. ");
            }            
            tx.commit();
            }
        catch(ConstraintViolationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, " An error has ocurred ton insert new values. ");
        }
        finally {
            session.close();        
        }
    }
    
    //DELETE a film in table film
    public void deleteFilm(Film film){
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {                                   
            tx = session.beginTransaction();   
            //Create sentence, for delete information of this film from table film_actor if exist
            Query query1 = session.createQuery("delete from FilmActor where film_id = :ID");
            query1.setParameter("ID", film.getFilmId());            
            int result1=query1.executeUpdate();
            //If result is distinct of 0, this film have any actor asociate
            if(result1<0){
                JOptionPane.showMessageDialog(null, "This film has delete correctly.");
            }           
            //Create sentence for delete film from table film
            Query query = session.createQuery("delete from Film where film_id = :ID");
            //Insert Id value in query
            query.setParameter("ID", film.getFilmId());            
            int result2 = query.executeUpdate(); 
            //If result is more bigger than 0, this actor have any film
            if (result2 > 0) {
                JOptionPane.showMessageDialog(null, "Delete all Film information correctly . ");
            }                 
            tx.commit();            
        }
        catch(ConstraintViolationException e) {
            e.printStackTrace();    
            System.out.println("ERROR "+e);            
        }
        finally {
            session.close();
        }
    }
    ////////////////////////////////////////////////////////////////////////////FINISH OF FILMS TABLE ACTIONS
    
    ////////////////////////////////////////////////////////////////////////////START TABLE FILMACTOR TO DO
    public void newFilmActor(FilmActor filmActor) {
        Session session = sessionFactory.openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            session.save(filmActor);
            tx.commit();
            JOptionPane.showMessageDialog(null, "Insert new actor and film in table FilmActor correctly.");
        }
        catch(ConstraintViolationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, " An error has ocurred ton insert new values. ");
        }
        finally {
            session.close();
        }
    }    
}
