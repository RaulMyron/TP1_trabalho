<<<<<<< HEAD
package telas.administracaoGestao.model;

import java.util.List;
import telas.administracaoGestao.model.Perfil;
// Importe suas classes de exceção personalizadas aqui
// import telas.administracaoGestao.model.exceptions.ValidationException;

public class Administrador extends Usuario {

    public Administrador(String nome, String cpf, String email, String login, String senha) {
        super(nome, cpf, email, login, senha);
        addPerfil(Perfil.ADMINISTRADOR);
    }

   /**
     * Método de negócio para cadastrar um novo usuário no sistema.
     * Este método agora funciona como uma "Fábrica" (Factory), criando a
     * instância correta (Gestor, Recrutador, etc.) baseado no perfil.
     *
     * @param todosOsUsuarios A lista de usuários do sistema (para verificar duplicidade)
     * @param nome O nome do novo usuário
     * @param cpf O CPF do novo usuário
     * @param email O email do novo usuário
     * @param login O login do novo usuário
     * @param senha A senha do novo usuário
     * @param perfilParaCriar O Perfil que este novo usuário terá (ex: Perfil.GESTOR)
     * @return O objeto Usuario que foi criado (seja ele um Gestor, Recrutador, etc.).
     * @throws Exception (Idealmente uma exceção personalizada, ex: ValidationException)
     */
    public Usuario cadastrarUsuario(List<Usuario> todosOsUsuarios, String nome, String cpf, 
                                    String email, String login, String senha, 
                                    Perfil perfilParaCriar) throws Exception {

        // VALIDAÇÃO DE CAMPOS NULOS OU VAZIOS ---
        if (nome == null || nome.trim().isEmpty() || 
            cpf == null || cpf.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            login == null || login.trim().isEmpty() || 
            senha == null || senha.isEmpty() ||
            perfilParaCriar == null) {
            
            throw new Exception("Erro: Todos os campos, incluindo o perfil, são obrigatórios.");
        }

        // VALIDAÇÃO DA REGRA DE NEGÓCIO: SENHA
        if (senha.length() < 8 || !senha.matches(".*[a-zA-Z].*") || !senha.matches(".*[0-9].*")) {
            throw new Exception("Erro: A senha deve ter no mínimo 8 caracteres, " +
                                  "contendo letras e números.");
        }

        // --- 3. VALIDAÇÃO DA REGRA DE NEGÓCIO: DADOS ÚNICOS ---
        for (Usuario u : todosOsUsuarios) {
            if (u.getCpf().equals(cpf)) {
                throw new Exception("Erro: O CPF '" + cpf + "' já está cadastrado.");
            }
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new Exception("Erro: O Email '" + email + "' já está cadastrado.");
            }
            if (u.getLogin().equalsIgnoreCase(login)) {
                throw new Exception("Erro: O Login '" + login + "' já está cadastrado.");
            }
        }
        
        // --- 4. CRIAÇÃO (O PONTO DA CORREÇÃO) ---
        Usuario novoUsuario; // Declara a variável do tipo "pai" (Usuario)

        switch (perfilParaCriar) {
            case ADMINISTRADOR:
                novoUsuario = new Administrador(nome, cpf, email, login, senha);
                break;
            case GESTOR:
                novoUsuario = new Gestor(nome, cpf, email, login, senha);
                break;
            // Adicione outros casos (como FUNCIONARIO) se eles puderem ser criados assim
            default:
                throw new Exception("Erro: Perfil '" + perfilParaCriar + "' não é válido para criação.");
        }
        
        // --- 5. PERSISTÊNCIA E RETORNO ---
        todosOsUsuarios.add(novoUsuario);
        
        System.out.println("Administrador cadastrou com sucesso: " + novoUsuario.getNome() + 
                           " com perfil " + perfilParaCriar);
        
        return novoUsuario;
    }
    public Usuario editarUsuario(Usuario usuarioParaEditar, List<Usuario> todosOsUsuarios, 
                                 String novoNome, String novoEmail, String novoLogin) throws Exception {

        // --- 1. VALIDAÇÃO DE CAMPOS NULOS OU VAZIOS ---
        if (novoNome == null || novoNome.trim().isEmpty() || 
            novoEmail == null || novoEmail.trim().isEmpty() ||
            novoLogin == null || novoLogin.trim().isEmpty()) {
            
            throw new Exception("Erro: Nenhum campo pode ficar vazio.");
        }

        // --- 2. VALIDAÇÃO DE DADOS ÚNICOS (Email e Login) ---
        // "Validar dados de usuários (por exemplo: CPF, email)."
        for (Usuario u : todosOsUsuarios) {
            
            // Verifica se o email novo JÁ EXISTE em outro usuário
            if (u.getEmail().equalsIgnoreCase(novoEmail)) {
                // Se o email pertence a OUTRO usuário, é um erro.
                // Se pertence ao usuário que estamos editando (u.equals(usuarioParaEditar)), 
                // não é um erro, ele apenas não mudou o email.
                if (!u.getCpf().equals(usuarioParaEditar.getCpf())) {
                    throw new Exception("Erro: O novo email '" + novoEmail + "' já pertence a outro usuário.");
                }
            }

            // Verifica se o login novo JÁ EXISTE em outro usuário
            if (u.getLogin().equalsIgnoreCase(novoLogin)) {
                if (!u.getCpf().equals(usuarioParaEditar.getCpf())) {
                    throw new Exception("Erro: O novo login '" + novoLogin + "' já pertence a outro usuário.");
                }
            }
        }
        
        // --- 3. ATUALIZAÇÃO DOS DADOS ---
        // Se todas as validações passaram, atualiza o objeto
        // Usa os setters herdados da classe Pessoa e Usuario
        usuarioParaEditar.setNome(novoNome);
        usuarioParaEditar.setEmail(novoEmail);
        usuarioParaEditar.setLogin(novoLogin); // (Assumindo que você criou setLogin() em Usuario)
        
        System.out.println("Administrador editou com sucesso: " + usuarioParaEditar.getNome());

        // --- 4. RETORNO ---
        return usuarioParaEditar;
    }
    public void excluirUsuario(Usuario usuarioParaRemover, List<Usuario> todosOsUsuarios, 
                               Administrador adminLogado) throws Exception {

        // --- 1. VALIDAÇÃO: NÃO PODE EXCLUIR A SI MESMO ---
        // Usamos o CPF (ou ID) para garantir que são o mesmo objeto.
        if (usuarioParaRemover.getCpf().equals(adminLogado.getCpf())) {
            throw new Exception("Erro: Você não pode excluir a sua própria conta de administrador.");
        }

        // --- 2. VALIDAÇÃO: NÃO PODE EXCLUIR O ÚLTIMO ADMINISTRADOR ---
        // Primeiro, verificamos se o usuário a ser removido é um administrador.
        if (usuarioParaRemover.getPerfis().contains(Perfil.ADMINISTRADOR)) {
            // Se sim, contamos quantos administradores existem no total.
            int contagemAdmins = 0;
            for (Usuario u : todosOsUsuarios) {
                if (u.getPerfis().contains(Perfil.ADMINISTRADOR)) {
                    contagemAdmins++;
                }
            }

            // Se só houver 1 admin (e estamos tentando excluí-lo), barramos a ação.
            if (contagemAdmins <= 1) {
                throw new Exception("Erro: Não é possível excluir o último administrador do sistema.");
            }
        }

        // --- 3. EXCLUSÃO ---
        // Se todas as validações passaram, remove o usuário da "base de dados".
        boolean removeu = todosOsUsuarios.remove(usuarioParaRemover);

        if (removeu) {
            System.out.println("Administrador excluiu com sucesso: " + usuarioParaRemover.getNome());
        } else {
            // Isso pode acontecer se o usuário não foi encontrado na lista por algum motivo.
            System.out.println("Erro: Não foi possível encontrar o usuário para exclusão.");
        }
    }
    public void definirPerfil(Usuario usuario, Perfil perfilParaAdicionar) throws Exception {

        // --- 1. VALIDAÇÃO: JÁ POSSUI O PERFIL? ---
        if (usuario.getPerfis().contains(perfilParaAdicionar)) {
            throw new Exception("Erro: O usuário '" + usuario.getNome() + 
                                  "' já possui o perfil " + perfilParaAdicionar);
        }

        // --- 2. ADIÇÃO ---
        // Chama o método "addPerfil" que deve existir na classe Usuario 
        usuario.addPerfil(perfilParaAdicionar);
        
        System.out.println("Perfil " + perfilParaAdicionar + 
                           " adicionado com sucesso para " + usuario.getNome());
    }
    /**
     * Método de negócio para REMOVER um perfil de um usuário.
     *
     * @param usuario O usuário que perderá o perfil.
     * @param perfilParaRemover O perfil a ser revogado (ex: Perfil.GESTOR).
     * @param todosOsUsuarios A lista completa de usuários (para validar o último admin).
     * @throws Exception
     */
    public void removerPerfil(Usuario usuario, Perfil perfilParaRemover, 
                              List<Usuario> todosOsUsuarios) throws Exception {

        // --- 1. VALIDAÇÃO: POSSUI O PERFIL? ---
        if (!usuario.getPerfis().contains(perfilParaRemover)) {
            throw new Exception("Erro: O usuário '" + usuario.getNome() + 
                                  "' não possui o perfil " + perfilParaRemover + " para ser removido.");
        }

        // --- 2. VALIDAÇÃO DE SEGURANÇA: ÚLTIMO ADMIN ---
        // Verifica se estamos tentando remover o perfil de ADMINISTRADOR
        if (perfilParaRemover == Perfil.ADMINISTRADOR) {
            
            // Contamos quantos administradores existem
            int contagemAdmins = 0;
            for (Usuario u : todosOsUsuarios) {
                if (u.getPerfis().contains(Perfil.ADMINISTRADOR)) {
                    contagemAdmins++;
                }
            }

            // Se este for o último admin, não podemos remover seu perfil
            if (contagemAdmins <= 1) {
                throw new Exception("Erro: Não é possível remover o perfil de ADMINISTRADOR " +
                                  "do último administrador do sistema.");
            }
        }

        // --- 3. REMOÇÃO ---
        // Chama o método "removePerfil" que deve existir na classe Usuario 
        usuario.removePerfil(perfilParaRemover);
        
        System.out.println("Perfil " + perfilParaRemover + 
                           " removido com sucesso de " + usuario.getNome());
    }

=======
package telas.administracaoGestao.model;

import java.util.List;
import telas.administracaoGestao.model.Perfil;
import java.io.Serializable;
// Importe suas classes de exceção personalizadas aqui
// import telas.administracaoGestao.model.exceptions.ValidationException;

public class Administrador extends Usuario implements Serializable { 
    
    private static final long serialVersionUID = 1L;

    public Administrador(String nome, String cpf, String email, String login, String senha) {
        super(nome, cpf, email, login, senha);
        addPerfil(Perfil.ADMINISTRADOR);
    }

   /**
     * Método de negócio para cadastrar um novo usuário no sistema.
     * Este método agora funciona como uma "Fábrica" (Factory), criando a
     * instância correta (Gestor, Recrutador, etc.) baseado no perfil.
     *
     * @param todosOsUsuarios A lista de usuários do sistema (para verificar duplicidade)
     * @param nome O nome do novo usuário
     * @param cpf O CPF do novo usuário
     * @param email O email do novo usuário
     * @param login O login do novo usuário
     * @param senha A senha do novo usuário
     * @param perfilParaCriar O Perfil que este novo usuário terá (ex: Perfil.GESTOR)
     * @return O objeto Usuario que foi criado (seja ele um Gestor, Recrutador, etc.).
     * @throws Exception (Idealmente uma exceção personalizada, ex: ValidationException)
     */
    public Usuario cadastrarUsuario(List<Usuario> todosOsUsuarios, String nome, String cpf, 
                                    String email, String login, String senha, 
                                    Perfil perfilParaCriar) throws Exception {

        // VALIDAÇÃO DE CAMPOS NULOS OU VAZIOS ---
        if (nome == null || nome.trim().isEmpty() || 
            cpf == null || cpf.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            login == null || login.trim().isEmpty() || 
            senha == null || senha.isEmpty() ||
            perfilParaCriar == null) {
            
            throw new Exception("Erro: Todos os campos, incluindo o perfil, são obrigatórios.");
        }

        // VALIDAÇÃO DA REGRA DE NEGÓCIO: SENHA
        if (senha.length() < 8 || !senha.matches(".*[a-zA-Z].*") || !senha.matches(".*[0-9].*")) {
            throw new Exception("Erro: A senha deve ter no mínimo 8 caracteres, " +
                                  "contendo letras e números.");
        }

        // --- 3. VALIDAÇÃO DA REGRA DE NEGÓCIO: DADOS ÚNICOS ---
        for (Usuario u : todosOsUsuarios) {
            if (u.getCpf().equals(cpf)) {
                throw new Exception("Erro: O CPF '" + cpf + "' já está cadastrado.");
            }
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new Exception("Erro: O Email '" + email + "' já está cadastrado.");
            }
            if (u.getLogin().equalsIgnoreCase(login)) {
                throw new Exception("Erro: O Login '" + login + "' já está cadastrado.");
            }
        }
        
        // --- 4. CRIAÇÃO (O PONTO DA CORREÇÃO) ---
        Usuario novoUsuario; // Declara a variável do tipo "pai" (Usuario)

        switch (perfilParaCriar) {
            case ADMINISTRADOR:
                novoUsuario = new Administrador(nome, cpf, email, login, senha);
                break;
            case GESTOR:
                novoUsuario = new Gestor(nome, cpf, email, login, senha);
                break;
            // Adicione outros casos (como FUNCIONARIO) se eles puderem ser criados assim
            default:
                throw new Exception("Erro: Perfil '" + perfilParaCriar + "' não é válido para criação.");
        }
        
        // --- 5. PERSISTÊNCIA E RETORNO ---
        todosOsUsuarios.add(novoUsuario);
        
        System.out.println("Administrador cadastrou com sucesso: " + novoUsuario.getNome() + 
                           " com perfil " + perfilParaCriar);
        
        return novoUsuario;
    }
    public Usuario editarUsuario(Usuario usuarioParaEditar, List<Usuario> todosOsUsuarios, 
                                 String novoNome, String novoEmail, String novoLogin) throws Exception {

        // --- 1. VALIDAÇÃO DE CAMPOS NULOS OU VAZIOS ---
        if (novoNome == null || novoNome.trim().isEmpty() || 
            novoEmail == null || novoEmail.trim().isEmpty() ||
            novoLogin == null || novoLogin.trim().isEmpty()) {
            
            throw new Exception("Erro: Nenhum campo pode ficar vazio.");
        }

        // --- 2. VALIDAÇÃO DE DADOS ÚNICOS (Email e Login) ---
        // "Validar dados de usuários (por exemplo: CPF, email)."
        for (Usuario u : todosOsUsuarios) {
            
            // Verifica se o email novo JÁ EXISTE em outro usuário
            if (u.getEmail().equalsIgnoreCase(novoEmail)) {
                // Se o email pertence a OUTRO usuário, é um erro.
                // Se pertence ao usuário que estamos editando (u.equals(usuarioParaEditar)), 
                // não é um erro, ele apenas não mudou o email.
                if (!u.getCpf().equals(usuarioParaEditar.getCpf())) {
                    throw new Exception("Erro: O novo email '" + novoEmail + "' já pertence a outro usuário.");
                }
            }

            // Verifica se o login novo JÁ EXISTE em outro usuário
            if (u.getLogin().equalsIgnoreCase(novoLogin)) {
                if (!u.getCpf().equals(usuarioParaEditar.getCpf())) {
                    throw new Exception("Erro: O novo login '" + novoLogin + "' já pertence a outro usuário.");
                }
            }
        }
        
        // --- 3. ATUALIZAÇÃO DOS DADOS ---
        // Se todas as validações passaram, atualiza o objeto
        // Usa os setters herdados da classe Pessoa e Usuario
        usuarioParaEditar.setNome(novoNome);
        usuarioParaEditar.setEmail(novoEmail);
        usuarioParaEditar.setLogin(novoLogin); // (Assumindo que você criou setLogin() em Usuario)
        
        System.out.println("Administrador editou com sucesso: " + usuarioParaEditar.getNome());

        // --- 4. RETORNO ---
        return usuarioParaEditar;
    }
    public void excluirUsuario(Usuario usuarioParaRemover, List<Usuario> todosOsUsuarios, 
                               Administrador adminLogado) throws Exception {

        // --- 1. VALIDAÇÃO: NÃO PODE EXCLUIR A SI MESMO ---
        // Usamos o CPF (ou ID) para garantir que são o mesmo objeto.
        if (usuarioParaRemover.getCpf().equals(adminLogado.getCpf())) {
            throw new Exception("Erro: Você não pode excluir a sua própria conta de administrador.");
        }

        // --- 2. VALIDAÇÃO: NÃO PODE EXCLUIR O ÚLTIMO ADMINISTRADOR ---
        // Primeiro, verificamos se o usuário a ser removido é um administrador.
        if (usuarioParaRemover.getPerfis().contains(Perfil.ADMINISTRADOR)) {
            // Se sim, contamos quantos administradores existem no total.
            int contagemAdmins = 0;
            for (Usuario u : todosOsUsuarios) {
                if (u.getPerfis().contains(Perfil.ADMINISTRADOR)) {
                    contagemAdmins++;
                }
            }

            // Se só houver 1 admin (e estamos tentando excluí-lo), barramos a ação.
            if (contagemAdmins <= 1) {
                throw new Exception("Erro: Não é possível excluir o último administrador do sistema.");
            }
        }

        // --- 3. EXCLUSÃO ---
        // Se todas as validações passaram, remove o usuário da "base de dados".
        boolean removeu = todosOsUsuarios.remove(usuarioParaRemover);

        if (removeu) {
            System.out.println("Administrador excluiu com sucesso: " + usuarioParaRemover.getNome());
        } else {
            // Isso pode acontecer se o usuário não foi encontrado na lista por algum motivo.
            System.out.println("Erro: Não foi possível encontrar o usuário para exclusão.");
        }
    }
    public void definirPerfil(Usuario usuario, Perfil perfilParaAdicionar) throws Exception {

        // --- 1. VALIDAÇÃO: JÁ POSSUI O PERFIL? ---
        if (usuario.getPerfis().contains(perfilParaAdicionar)) {
            throw new Exception("Erro: O usuário '" + usuario.getNome() + 
                                  "' já possui o perfil " + perfilParaAdicionar);
        }

        // --- 2. ADIÇÃO ---
        // Chama o método "addPerfil" que deve existir na classe Usuario 
        usuario.addPerfil(perfilParaAdicionar);
        
        System.out.println("Perfil " + perfilParaAdicionar + 
                           " adicionado com sucesso para " + usuario.getNome());
    }
    /**
     * Método de negócio para REMOVER um perfil de um usuário.
     *
     * @param usuario O usuário que perderá o perfil.
     * @param perfilParaRemover O perfil a ser revogado (ex: Perfil.GESTOR).
     * @param todosOsUsuarios A lista completa de usuários (para validar o último admin).
     * @throws Exception
     */
    public void removerPerfil(Usuario usuario, Perfil perfilParaRemover, 
                              List<Usuario> todosOsUsuarios) throws Exception {

        // --- 1. VALIDAÇÃO: POSSUI O PERFIL? ---
        if (!usuario.getPerfis().contains(perfilParaRemover)) {
            throw new Exception("Erro: O usuário '" + usuario.getNome() + 
                                  "' não possui o perfil " + perfilParaRemover + " para ser removido.");
        }

        // --- 2. VALIDAÇÃO DE SEGURANÇA: ÚLTIMO ADMIN ---
        // Verifica se estamos tentando remover o perfil de ADMINISTRADOR
        if (perfilParaRemover == Perfil.ADMINISTRADOR) {
            
            // Contamos quantos administradores existem
            int contagemAdmins = 0;
            for (Usuario u : todosOsUsuarios) {
                if (u.getPerfis().contains(Perfil.ADMINISTRADOR)) {
                    contagemAdmins++;
                }
            }

            // Se este for o último admin, não podemos remover seu perfil
            if (contagemAdmins <= 1) {
                throw new Exception("Erro: Não é possível remover o perfil de ADMINISTRADOR " +
                                  "do último administrador do sistema.");
            }
        }

        // --- 3. REMOÇÃO ---
        // Chama o método "removePerfil" que deve existir na classe Usuario 
        usuario.removePerfil(perfilParaRemover);
        
        System.out.println("Perfil " + perfilParaRemover + 
                           " removido com sucesso de " + usuario.getNome());
    }

>>>>>>> 2e686e0 (nati mudanças)
} // Fim da classe Administrador