package com.morgan.server.account;

import com.google.common.base.Supplier;
import com.google.inject.ImplementedBy;

/**
 * A utility class for helping create new temporary passwords.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@ImplementedBy(DefaultPasswordCreator.class)
interface PasswordCreator extends Supplier<String> {
}
