package com.morgan.server.mtg;

import java.io.Serializable;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Represents a bunch of text that might also contain symbols inside.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class TextWithSymbols implements Serializable {

  static final long serialVersionUID = 0L;

  public enum NodeType {
    TEXT(String.class),
    MANA_SYMBOL(ManaSymbol.class),
    OTHER_SYMBOL(OtherSymbol.class);

    private final Class<?> valueType;

    private NodeType(Class<?> valueType) {
      this.valueType = Preconditions.checkNotNull(valueType);
    }

    public boolean isCompatibleWith(Object value) {
      return valueType.isAssignableFrom(value.getClass());
    }
  }

  /**
   * A node in a {@link TextWithSymbols} instance.
   *
   * @param <T> the type of content in the node ({@link String}, {@link ManaSymbol}, or
   *     {@link OtherSymbol}.
   */
  public static final class Node<T> implements Serializable {

    static final long serialVersionUID = 0L;

    private final NodeType nodeType;
    private final T value;

    private Node(NodeType nodeType, T value) {
      this.nodeType = Preconditions.checkNotNull(nodeType);
      this.value = Preconditions.checkNotNull(value);
      Preconditions.checkArgument(nodeType.isCompatibleWith(value));
    }

    public NodeType getNodeType() {
      return nodeType;
    }

    public T getValue() {
      return value;
    }

    @Override public int hashCode() {
      return Objects.hash(nodeType, value);
    }

    @Override public boolean equals(Object o) {
      if (o == this) {
        return true;
      }

      if (!(o instanceof Node)) {
        return false;
      }

      Node<?> other = (Node<?>) o;
      return nodeType == other.nodeType
          && Objects.equals(value, other.value);
    }

    @Override public String toString() {
      return MoreObjects.toStringHelper(Node.class)
          .add("nodeType", nodeType)
          .add("value", value)
          .toString();
    }

    public static Node<String> createTextNode(String text) {
      Preconditions.checkNotNull(text);
      return new Node<>(NodeType.TEXT, text);
    }

    public static Node<ManaSymbol> createManaSymbolNode(ManaSymbol symbol) {
      Preconditions.checkNotNull(symbol);
      return new Node<>(NodeType.MANA_SYMBOL, symbol);
    }

    public static Node<OtherSymbol> createOtherSymbolNode(OtherSymbol symbol) {
      Preconditions.checkNotNull(symbol);
      return new Node<>(NodeType.OTHER_SYMBOL, symbol);
    }
  }

  /**
   * Interface for a type that a client can use to visit nodes in this object.
   */
  public interface NodeVisitor {
    void visitTextNode(String text);
    void visitManaSymbolNode(ManaSymbol symbol);
    void visitOtherSymbolNode(OtherSymbol symbol);
  }

  private final ImmutableList<Node<?>> nodes;

  private TextWithSymbols(Iterable<Node<?>> nodes) {
    this.nodes = ImmutableList.copyOf(nodes);
  }

  public ImmutableList<Node<?>> getNodes() {
    return nodes;
  }

  public void visitWith(NodeVisitor visitor) {
    Preconditions.checkNotNull(visitor);
    for (Node<?> node : nodes) {
      switch (node.getNodeType()) {
        case TEXT :
          visitor.visitTextNode((String) node.getValue());
          break;

        case MANA_SYMBOL :
          visitor.visitManaSymbolNode((ManaSymbol) node.getValue());
          break;

        case OTHER_SYMBOL :
          visitor.visitOtherSymbolNode((OtherSymbol) node.getValue());
          break;
      }
    }
  }

  @Override public int hashCode() {
    return nodes.hashCode();
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof TextWithSymbols)) {
      return false;
    }

    return nodes.equals(((TextWithSymbols) o).nodes);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(TextWithSymbols.class)
        .add("nodes", nodes)
        .toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for building up a {@link TextWithSymbols} instance.
   */
  public static class Builder {

    private final ImmutableList.Builder<Node<?>> nodesBuilder = ImmutableList.builder();

    private Builder() {
    }

    public Builder addTextNode(String text) {
      nodesBuilder.add(Node.createTextNode(text));
      return this;
    }

    public Builder addManaSymbolNode(ManaSymbol symbol) {
      nodesBuilder.add(Node.createManaSymbolNode(symbol));
      return this;
    }

    public Builder addOtherSymbolNode(OtherSymbol symbol) {
      nodesBuilder.add(Node.createOtherSymbolNode(symbol));
      return this;
    }

    public TextWithSymbols build() {
      return new TextWithSymbols(nodesBuilder.build());
    }
  }
}
