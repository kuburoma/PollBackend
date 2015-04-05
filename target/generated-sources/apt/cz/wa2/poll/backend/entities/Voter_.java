package cz.wa2.poll.backend.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Voter.class)
public abstract class Voter_ {

	public static volatile SingularAttribute<Voter, String> firstName;
	public static volatile SingularAttribute<Voter, String> lastName;
	public static volatile SingularAttribute<Voter, String> password;
	public static volatile ListAttribute<Voter, Ballot> ballots;
	public static volatile ListAttribute<Voter, VoterGroup> voterGroups;
	public static volatile SingularAttribute<Voter, Long> id;
	public static volatile ListAttribute<Voter, VoterGroup> supervisedGroups;
	public static volatile SingularAttribute<Voter, String> email;

}

