package cz.wa2.poll.backend.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Poll.class)
public abstract class Poll_ {

	public static volatile ListAttribute<Poll, Ballot> ballots;
	public static volatile SingularAttribute<Poll, String> question;
	public static volatile SingularAttribute<Poll, String> name;
	public static volatile SingularAttribute<Poll, Long> id;
	public static volatile SingularAttribute<Poll, VoterGroup> voterGroup;

}

