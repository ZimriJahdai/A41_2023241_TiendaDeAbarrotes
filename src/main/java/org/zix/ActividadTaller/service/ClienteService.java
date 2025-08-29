package org.zix.ActividadTaller.service;

import org.zix.ActividadTaller.model.Cliente;
import org.zix.ActividadTaller.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente guardarCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con ese email");
        }
        return clienteRepository.save(cliente);
    }

    public List<Cliente> obtenerTodosClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarClientePorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }

    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!cliente.getEmail().equals(clienteActualizado.getEmail()) &&
                clienteRepository.existsByEmail(clienteActualizado.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con ese email");
        }

        cliente.setNombre(clienteActualizado.getNombre());
        cliente.setEmail(clienteActualizado.getEmail());
        cliente.setTelefono(clienteActualizado.getTelefono());

        return clienteRepository.save(cliente);
    }
}