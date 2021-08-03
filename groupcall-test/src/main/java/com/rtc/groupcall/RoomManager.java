package com.rtc.groupcall;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.rtc.groupcall.api.dto.RoomDto;
import com.rtc.groupcall.db.entity.RoomEntity;
import com.rtc.groupcall.db.repository.RoomRepository;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
@Service
public class RoomManager {

    private final Logger log = LoggerFactory.getLogger(RoomManager.class);

    @Autowired
    private KurentoClient kurento;

    @Autowired
    RoomRepository roomRepository;

    private final ConcurrentMap<Long, Room> rooms = new ConcurrentHashMap<>();

    public List<RoomEntity> getRooms(Long OfficeId){
        List<RoomEntity> roomEntitys = roomRepository.findAllByOfficeId(OfficeId);

        for(RoomEntity r: roomEntitys){
            if(rooms.get(r.getRoomId()) != null)
                continue;

            rooms.put(r.getRoomId(),  new Room(r.getRoomName(), r.getRoomId()));
        }

        return roomRepository.findAllByOfficeId(OfficeId);
    }

    public RoomEntity createRoom(RoomDto roomDto){
        RoomEntity roomEntity = RoomEntity.builder()
                .roomName(roomDto.getRoomName())
                .officeId(roomDto.getOfficeId())
                .userId(roomDto.getUserId())
                .build();
        return roomRepository.save(roomEntity);
    }

    public RoomEntity getRoom(Long roomId){
        return roomRepository.findByRoomId(roomId);
    }

    public RoomEntity updateRoom(RoomDto roomDto){
        RoomEntity entity = getRoom(roomDto.getRoomId());
        entity.setRoomName(roomDto.getRoomName());
        return roomRepository.save(entity);
    }

    public Room getRoom(String roomName, Long roomId) {
        log.info("{}찾기 시도, roomId = {}", roomName, roomId);
        Room room = rooms.get(roomId);

        if (room == null) {
            log.info("{}이/가 없다, 새로 생성!", roomName);
            room = new Room(roomName, kurento.createMediaPipeline(), roomId);
            rooms.put(roomId, room);
        }else if(room.getPipeline() == null){
            log.info("{}에 pipeline이 없다!. create pipeline!", roomName);
            room.setPipeline(kurento.createMediaPipeline());
        }
        log.info("{}을 찾음!, roomId = {}", roomName, roomId);
        log.info(room.toString());
        return room;
    }

    /**
     * Removes a room from the list of available rooms.
     *
     * @param room
     *          the room to be removed
     */
    public void removeRoom(Room room) {
        this.rooms.remove(room.getRoomId());
        room.close();
        log.info("Room {} removed and closed, roomId = {}", room.getRoomName(), room.getRoomId());
    }

}
