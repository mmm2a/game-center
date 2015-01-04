package com.morgan.server.email;

import com.morgan.server.util.flag.Flag;
import com.morgan.server.util.flag.FlagAccessor;

/**
 * Flag accessor for the email package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface EmailFlagAccessor extends FlagAccessor {

  @Flag(name = "smtp-host",
      description = "Hostname for the SMTP server to use when sending email",
      required = true)
  String smtpHostname();

  @Flag(name = "smtp-port",
      description = "Port on the SMTP host to connect to when sending email",
      required = true)
  int smtpPort();

  @Flag(name = "is-smtp-ssl-on-connect",
      description = "Boolean flag indicating whether or not to use SSL when connecting to the "
          + "SMTP server to send an email",
      required = false,
      defaultValue = "true")
  boolean isSslOnConnect();

  @Flag(name = "smtp-from-address",
      description = "FROM email address to use when sending email",
      required = true)
  String fromAddress();

  @Flag(name = "smtp-auth-username",
      description = "Username to log in to the SMTP server with",
      required = false)
  String smtpUserName();

  @Flag(name = "smtp-auth-password",
      description = "Password to use when logging in to the SMTP server to send email",
      required = false)
  String smtpPassword();
}
