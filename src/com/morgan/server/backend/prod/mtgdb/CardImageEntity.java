package com.morgan.server.backend.prod.mtgdb;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.morgan.server.mtg.ImageType;

/**
 * Entity representing an image in a card.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Entity(name = "mtgcardimage")
class CardImageEntity {

  @Id @GeneratedValue
  private long id;

  @Column(length = 32, nullable = false, unique = false)
  private String multiverseId;

  @Enumerated(EnumType.STRING)
  @Column(length = 16, nullable = false, unique = false)
  private ImageType imageType;

  @Lob
  @Basic(
      fetch = FetchType.LAZY,
      optional = false)
  private byte[] image;

  CardImageEntity() {
  }

  CardImageEntity(String multiverseId, ImageType imageType, byte[] image) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(multiverseId));
    Preconditions.checkNotNull(imageType);
    Preconditions.checkNotNull(image);

    this.multiverseId = multiverseId;
    this.imageType = imageType;
    this.image = image;
  }

  long getId() {
    return id;
  }

  String getMultiverseId() {
    return multiverseId;
  }

  ImageType getImageType() {
    return imageType;
  }

  byte[] getImage() {
    return image;
  }
}
