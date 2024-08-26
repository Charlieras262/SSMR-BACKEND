package com.g5.ssmr.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SSHConnector {

    private static final String ENTER_KEY = "n";
    private Session session;

    /**
     * Establece una conexión SSH.
     *
     * @param username Nombre de usuario.
     * @param keyPath  Contraseña.
     * @param host     Host a conectar.
     * @param port     Puerto del Host.
     * @throws JSchException          Cualquier error al establecer
     *                                conexión SSH.
     * @throws IllegalAccessException Indica que ya existe una conexión
     *                                SSH establecida.
     */
    public void connect(String host, String username, int port, String keyPath)
            throws JSchException, IllegalAccessException {
        if (this.session == null || !this.session.isConnected()) {
            JSch jsch = new JSch();
            jsch.addIdentity(keyPath);

            this.session = jsch.getSession(username, host, port);

            this.session.setConfig("StrictHostKeyChecking", "no");

            this.session.connect();
        } else {
            throw new IllegalAccessException("Sesion SSH ya iniciada.");
        }
    }

    /**
     * Ejecuta un comando SSH.
     *
     * @param command Comando SSH a ejecutar.
     * @return Resultado de la ejecución del comando.
     * @throws IllegalAccessException Excepción lanzada cuando no hay
     *                                conexión establecida.
     * @throws JSchException          Excepción lanzada por algún
     *                                error en la ejecución del comando
     *                                SSH.
     * @throws IOException            Excepción al leer el texto arrojado
     *                                luego de la ejecución del comando
     *                                SSH.
     */
    public final String executeCommand(String command)
            throws IllegalAccessException, JSchException, IOException {
        if (this.session != null && this.session.isConnected()) {

            ChannelExec channelExec = (ChannelExec) this.session.
                    openChannel("exec");

            InputStream in = channelExec.getInputStream();

            channelExec.setCommand(command);
            channelExec.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder builder = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                builder.append(linea);
                builder.append(ENTER_KEY);
            }

            channelExec.disconnect();

            return builder.toString();
        } else {
            throw new IllegalAccessException("No existe sesion SSH iniciada.");
        }
    }

    /**
     * Cierra la sesión SSH.
     */
    public final void disconnect() {
        this.session.disconnect();
    }

    public boolean isConnected() {
        return session != null && session.isConnected();
    }
}
