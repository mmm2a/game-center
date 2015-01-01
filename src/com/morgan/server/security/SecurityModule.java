package com.morgan.server.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.morgan.server.common.CommonBindingAnnotations.DeobfuscationCipher;
import com.morgan.server.common.CommonBindingAnnotations.ObfuscationCipher;
import com.morgan.server.common.CommonBindingAnnotations.Obfuscator;
import com.morgan.server.util.flag.FlagAccessorFactory;

/**
 * A GUICE module for the security package on the server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class SecurityModule extends AbstractModule {

  @Override protected void configure() {
  }

  @Provides @Singleton protected SecurityFlagAccessor provideSecurityFlagAccessor(
      FlagAccessorFactory accessorFactory) {
    return accessorFactory.getFlagAccessor(SecurityFlagAccessor.class);
  }

  @Provides @Singleton protected KeyStore provideKeyStore(SecurityFlagAccessor flagAccessor)
      throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException,
          CertificateException {
    KeyStore store = KeyStore.getInstance(flagAccessor.keystoreType());

    try (InputStream in = new FileInputStream(flagAccessor.keystorePath())) {
      store.load(in, flagAccessor.keystorePassword().toCharArray());
    }

    return store;
  }

  @Provides @Singleton @Obfuscator
  protected SecretKey provideObfuscationKey(KeyStore keyStore, SecurityFlagAccessor flagAccessor)
      throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
    Key key = keyStore.getKey(
        flagAccessor.obfuscatorAlias(), flagAccessor.obfuscatorPassword().toCharArray());
    Preconditions.checkState(key instanceof SecretKey);
    return (SecretKey) key;
  }

  @Provides @ObfuscationCipher protected Cipher provideObfuscationCipher(
      @Obfuscator SecretKey key)
          throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    // TODO(morgan): Normally, we'd want to use CBC instead of ECB, but then we have to track the
    // IV for the cipher, and I'm not ready to do that.
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return cipher;
  }

  @Provides @DeobfuscationCipher protected Cipher provideDeobfuscationCipher(
      @Obfuscator SecretKey key)
          throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    // TODO(morgan): Normally, we'd want to use CBC instead of ECB, but then we have to track the
    // IV for the cipher, and I'm not ready to do that.
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
    cipher.init(Cipher.DECRYPT_MODE, key);
    return cipher;
  }
}
