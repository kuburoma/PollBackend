package cz.wa2.poll.backend.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ballot.class)
public abstract class Ballot_ {

	public static volatile SingularAttribute<Ballot, Long> answer;
	public static volatile SingularAttribute<Ballot, Long> id;
	public static volatile SingularAttribute<Ballot, Poll> poll;
	public static volatile SingularAttribute<Ballot, Voter> voter;

}

