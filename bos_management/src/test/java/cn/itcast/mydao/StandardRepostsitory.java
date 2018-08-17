package cn.itcast.mydao;

import java.util.List;

import cn.itcast.bos.domain.base.Standard;

public interface StandardRepostsitory extends JpaRep<Standard> {

	public List<Standard> findByUsername(String username);

	@Query(value = "from Standard where username = ?")
	public List<Standard> queryUsername(String username);

}
