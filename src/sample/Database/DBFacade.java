package sample.Database;

import sample.model.*;

import java.util.List;

public class DBFacade {

    private static DBFacade dbFacade;

    private PatientDB patientDB;
    private GroupDB groupDB;
    private UserDB userDB;
    private ViewDB viewDB;
    private VisitDB visitDB;

    private DBFacade() {
        patientDB = new PatientDB();
        groupDB = new GroupDB();
        userDB = new UserDB();
        viewDB = new ViewDB();
        visitDB = new VisitDB();
    }

    public static DBFacade getDb() {
        if (dbFacade == null) {
            dbFacade = new DBFacade();
        }
        return dbFacade;
    }

    public Object[] registerPatient(Patient patient) {
        return patientDB.register(patient);
    }
    public Group selectGroup(String id) {
        return groupDB.selectGroup(id);
    }
    public User userLogin(String username, String password) {
        return userDB.login(username, password);
    }
    public Patient patientLogin(String username, String password) {
        return patientDB.login(username, password);
    }
    public List<View> getViews() {
        return viewDB.getViews();
    }
    public void addGroup(Group group) {
        groupDB.addGroup(group);
    }
    public List<Group> selectGroups() {
        return groupDB.selectGroups();
    }
    public String selectViewName(String id) {
        return viewDB.getViewName(id);
    }
    public List<User> selectUsers() {
        return userDB.selectUsers();
    }
    public boolean addUser(User user) {
        return userDB.addUser(user);
    }
    public View getView(String id) {
        return viewDB.getView(id);
    }
    public List<Patient> selectPatients() {
        return patientDB.findAll();
    }
    public List<Visit> getVisits(Patient patient) {
        return visitDB.getVisits(patient);
    }
    public List<Patient> searchPatients(String value) {
        return patientDB.searchPatients(value);
    }
    public Patient findPatient(String id) {
        return patientDB.findPatient(id);
    }
    public List<Visit> allVisits() {
        return visitDB.allVisits();
    }
    public void insertVisit(Visit visit) {
        visitDB.insertVisit(visit);
    }
    public long updateVisitDate(Visit visit, String newDate) {
        return visitDB.updateVisitDate(visit, newDate);
    }
    public long updateVisitNotes(Visit visit,String newNotes){
        return visitDB.updateVisitNotes(visit,newNotes);
    }
    public long deleteVisit(Visit visit) {
        return visitDB.deleteVisit(visit);
    }
    public long updateGroupViews(Group group,List<String> views_id){
        return groupDB.updateGroupViews(group,views_id);
    }
    public long deleteGroup(Group group) {
        return groupDB.deleteGroup(group);
    }
    public int findUsersBelongTo(Group group){
        return userDB.findUsersBelongsTo(group);
    }
    public long deleteUser(User user){
        return userDB.deleteUser(user);
    }
    public long updateUserWithPassword(User user,User newUser){
        return userDB.updateUserWithPassword(user,newUser);
    }
    public long updateUserWithoutPassword(User user,User newUser){
        return userDB.updateUserWithoutPassword(user,newUser);
    }
}
