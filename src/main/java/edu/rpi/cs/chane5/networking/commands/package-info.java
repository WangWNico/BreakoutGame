/**
 * Package contains the {@link edu.rpi.cs.chane5.networking.commands.Command} for building example_commands to be used in the protocol
 * between the {@link edu.rpi.cs.chane5.networking.connection.Server} and {@link edu.rpi.cs.chane5.networking.connection.Client}.
 * All example_commands need to be registered with the {@link edu.rpi.cs.chane5.networking.commands.Registry} using
 * {@link edu.rpi.cs.chane5.networking.commands.Registry#register(edu.rpi.cs.chane5.networking.commands.Command)} before
 * it will be acknowledged by the {@link edu.rpi.cs.chane5.networking.connection.Connection#receive()} code.
 *
 * @version {@value edu.rpi.cs.chane5.networking.commands.Command#VERSION}
 */
package edu.rpi.cs.chane5.networking.commands;
