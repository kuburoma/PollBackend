package cz.wa2.poll.backend.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VoterGroup.class)
public abstract class VoterGroup_ {

	public static volatile ListAttribute<VoterGroup, Voter> voters;
	public static volatile SingularAttribute<VoterGroup, String> name;
	public static volatile SingularAttribute<VoterGroup, String> description;
	public static volatile ListAttribute<VoterGroup, Poll> polls;
	public static volatile SingularAttribute<VoterGroup, Long> id;
	public static volatile SingularAttribute<VoterGroup, Voter> supervisor;

}

