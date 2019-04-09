package ru.fagci.tuihome.lib;

public class Res<T> {
  protected static int __id;
  protected final int _id = __id++;
  protected final String name;

  protected final T content;

  protected long createdAt = System.currentTimeMillis();
  protected long modifiedAt = this.createdAt;

  public Res(String name, T content) {
    this.name = name;
    this.content = content;
  }

  public long getCreatedAt() {
    return createdAt;
  }
  
  public long getModifiedAt() {
    return modifiedAt;
  }

  public T getContent() {
    return content;
  }

  @Override
  public String toString() {
    return name + "#" + _id;
  }

  public String getName() {
    return name;
  }

  protected void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  protected void setModifiedAt(long modifiedAt) {
    this.modifiedAt = modifiedAt;
  }
}
