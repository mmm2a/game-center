package com.morgan.server.security;

import java.nio.ByteBuffer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import com.google.common.io.BaseEncoding;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.server.common.CommonBindingAnnotations.DeobfuscationCipher;
import com.morgan.server.common.CommonBindingAnnotations.ObfuscationCipher;
import com.morgan.server.util.log.AdvancedLogger;

/**
 * Default implementation of the {@link AuthenticationCookieObfuscator}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultAuthenticationCookieObfuscator implements AuthenticationCookieObfuscator {

  private static final AdvancedLogger LOG =
      new AdvancedLogger(DefaultAuthenticationCookieObfuscator.class);

  private final Provider<Cipher> obfuscationCipherProvider;
  private final Provider<Cipher> deobfuscationCipherProvider;

  @Inject DefaultAuthenticationCookieObfuscator(
      @ObfuscationCipher Provider<Cipher> obfuscationCipherProvider,
      @DeobfuscationCipher Provider<Cipher> deobfuscationCipher) {
    this.obfuscationCipherProvider = obfuscationCipherProvider;
    this.deobfuscationCipherProvider = deobfuscationCipher;
  }

  @Override public String obfuscateAuthenticationCookie(byte[] clear) {
    Cipher cipher = obfuscationCipherProvider.get();
    ByteBuffer inputBuffer = ByteBuffer.wrap(clear);
    ByteBuffer outputBuffer = ByteBuffer.allocate(cipher.getOutputSize(clear.length));
    try {
      cipher.doFinal(inputBuffer, outputBuffer);
      return BaseEncoding.base64().encode(outputBuffer.array());
    } catch (IllegalBlockSizeException | BadPaddingException | ShortBufferException e) {
      LOG.error(e, "Unable to encrypt authentication token");
      throw new RuntimeException("Unable to encrypt authentication token", e);
    }
  }

  @Override public byte[] deobfuscateId(String encrypted) {
    Cipher cipher = deobfuscationCipherProvider.get();
    ByteBuffer input = ByteBuffer.wrap(BaseEncoding.base64().decode(encrypted));
    ByteBuffer output = ByteBuffer.allocate(cipher.getOutputSize(input.limit()));
    try {
      cipher.doFinal(input, output);
      return output.array();
    } catch (IllegalBlockSizeException | BadPaddingException | ShortBufferException e) {
      LOG.error(e, "Unable to decrypt authentication token");
      throw new RuntimeException("Unable to decrypt authentication token", e);
    }
  }
}
