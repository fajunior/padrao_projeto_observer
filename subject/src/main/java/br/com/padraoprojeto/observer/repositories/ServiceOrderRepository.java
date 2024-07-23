package br.com.padraoprojeto.observer.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.padraoprojeto.observer.entities.ServiceOrder;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Integer> {
	@Query("select so from ServiceOrder so where so.lastUpdate > :date and processed = false")
	List<ServiceOrder> findByLastUpdatedBiggerDate(@Param("date") LocalDateTime date);
	
	@Query("select so from ServiceOrder so where processed = false")
	List<ServiceOrder> findByProcessedFalse();
	
}