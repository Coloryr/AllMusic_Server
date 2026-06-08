package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes;

import java.util.List;

public interface Box {

    Box getParent();

    /**
     * Returns the size of this box including its header.
     *
     * @return this box's size
     */
    long getSize();

    /**
     * Returns the type of this box as a 4CC converted to a long.
     *
     * @return this box's type
     */
    long getType();

    /**
     * Returns the offset of this box in the stream/file. This is needed as a
     * seek point for random access.
     *
     * @return this box's offset
     */
    long getOffset();

    /**
     * Returns the name of this box as a human-readable string
     * (e.g. "Track Header Box").
     *
     * @return this box's name
     */
    String getName();

    /**
     * Indicates if this box has children.
     *
     * @return true if this box contains at least one child
     */
    boolean hasChildren();

    /**
     * Indicated if the box has a child with the given type.
     *
     * @param type the type of child box to look for
     * @return true if this box contains at least one child with the given type
     */
    boolean hasChild(long type);

    /**
     * Returns an ordered and unmodifiable list of all direct children of this
     * box. The list does not contain the children's children.
     *
     * @return this box's children
     */
    List<Box> getChildren();

    /**
     * Returns an ordered and unmodifiable list of all direct children of this
     * box with the specified type. The list does not contain the children's
     * children. If there is no child with the given type, the list will be
     * empty.
     *
     * @param type the type of child boxes to look for
     * @return this box's children with the given type
     */
    List<Box> getChildren(long type);

    /**
     * Returns the child box with the specified type. If this box has no child
     * with the given type, null is returned. To check if there is such a child
     * <code>hasChild(type)</code> can be used.
     * If more than one child exists with the same type, the first one will
     * always be returned. A list of all children with that type can be received
     * via <code>getChildren(type)</code>.
     *
     * @param type the type of child box to look for
     * @return the first child box with the given type, or null if none is found
     */
    Box getChild(long type);
}
