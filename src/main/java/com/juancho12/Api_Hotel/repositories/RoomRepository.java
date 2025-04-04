package com.juancho12.Api_Hotel.repositories;

import com.juancho12.Api_Hotel.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
