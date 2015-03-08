package com.morgan.server.security;

import java.nio.ByteBuffer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.server.common.CommonBindingAnnotations.DeobfuscationCipher;
import com.morgan.server.common.CommonBindingAnnotations.ObfuscationCipher;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;

/**
 * A class that can obfuscate and deobfuscate user ids.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultUserIdObfuscator implements UserIdObfuscator {

  private final Provider<Cipher> obfuscationCipherProvider;
  private final Provider<Cipher> deobfuscationCipherProvider;

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  @Inject DefaultUserIdObfuscator(
      @ObfuscationCipher Provider<Cipher> obfuscationCipherProvider,
      @DeobfuscationCipher Provider<Cipher> deobfuscationCipher) {
    this.obfuscationCipherProvider = obfuscationCipherProvider;
    this.deobfuscationCipherProvider = deobfuscationCipher;
  }

  private byte[] toByteArray(long id) {
    ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.putLong(id);
    buffer.flip();
    byte[] ret = new byte[buffer.remaining()];
    Preconditions.checkState(buffer.remaining() == 8);
    buffer.get(ret);
    return ret;
  }

  private long fromByteArray(byte[] id) {
    Preconditions.checkArgument(id.length == 8);
    ByteBuffer buffer = ByteBuffer.wrap(id);
    return buffer.getLong();
  }

  private String toString(byte[] array) {
    return BaseEncoding.base64().encode(array);
  }

  private byte[] fromString(String str) {
    return BaseEncoding.base64().decode(str);
  }

  @Override public String obfuscateId(long id) {
    byte[] clear = toByteArray(id);
    Cipher cipher = obfuscationCipherProvider.get();
    try {
      return toString(cipher.doFinal(clear));
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      log.error(e, "Unable to encrypt user id %d", id);
      throw new RuntimeException("Unable to encrypt user id", e);
    }
  }

  @Override public long deobfuscateId(String id) {
    byte[] encrypted = fromString(id);
    Cipher cipher = deobfuscationCipherProvider.get();
    try {
      return fromByteArray(cipher.doFinal(encrypted));
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      log.error(e, "Unable to decrypt user id %s", id);
      throw new RuntimeException("Unable to decrypt user id", e);
    }
  }
}
