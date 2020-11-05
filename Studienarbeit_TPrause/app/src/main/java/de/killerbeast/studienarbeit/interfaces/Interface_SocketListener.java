package de.killerbeast.studienarbeit.interfaces;

public interface Interface_SocketListener {

    void received(String received);

    default void warn(){}

}
