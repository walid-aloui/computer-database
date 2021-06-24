package com.excilys.cdb.binding.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.excilys.cdb.core.model.QUser;
import com.excilys.cdb.core.model.User;
import com.querydsl.core.Tuple;

@Component
public class MapperUser {

	private static final QUser qUser = QUser.user;

	public MapperUser() {
		super();
	}

	public User fromTupleToUser(Tuple t) {
		return new User(t.get(qUser.login), 
						t.get(qUser.password), 
						t.get(qUser.role));
	}

	public List<User> fromTupleListToUserList(List<Tuple> tupleList) {
		return tupleList
				.stream()
				.map(tuple -> fromTupleToUser(tuple))
				.collect(Collectors.toList());
	}

}
