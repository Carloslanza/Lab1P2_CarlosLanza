import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.Period;

class Usuario {
    String nombre;
    String apellido;
    String fechaNacimiento;
    String correo;
    String contraseña;

    public Usuario(String nombre, String apellido, String fechaNacimiento, String correo, String contraseña) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.contraseña = contraseña;
    }
}

public class SistemaRegistro {

    private ArrayList<Usuario> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        SistemaRegistro sistema = new SistemaRegistro();
        sistema.menuPrincipal();
    }

    public void menuPrincipal() {
        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Registrar Usuario");
            System.out.println("2. Listar Todo");
            System.out.println("3. Listar Por Dominio");
            System.out.println("4. Salir");

            int opcion = InputHelper.getInt("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    registrarUsuario();
                    break;
                case 2:
                    listarTodo();
                    break;
                case 3:
                    listarPorDominio();
                    break;
                case 4:
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }
        }
    }

    private void registrarUsuario() {
        String nombre = InputHelper.getString("Ingrese su nombre: ");
        String apellido = InputHelper.getString("Ingrese su apellido: ");
        String fechaNacimiento = InputHelper.getString("Ingrese su fecha de nacimiento (YYYY-MM-DD): ");
        String correo = InputHelper.getString("Ingrese su correo electrónico: ");
        String contraseña = InputHelper.getString("Ingrese su contraseña: ");

        if (!validarFechaNacimiento(fechaNacimiento)) {
            System.out.println("Debe tener al menos 13 años para registrarse.");
            return;
        }

        if (!validarCorreo(correo)) {
            System.out.println("Dirección de correo inválida o ya registrada.");
            return;
        }

        if (!validarContraseña(contraseña)) {
            System.out.println("Contraseña inválida.");
            return;
        }

        Usuario usuario = new Usuario(nombre, apellido, fechaNacimiento, correo, contraseña);
        usuarios.add(usuario);
        System.out.println("Usuario registrado con éxito.");
    }

    private void listarTodo() {
        for (Usuario usuario : usuarios) {
            imprimirUsuario(usuario);
        }
    }

    private void listarPorDominio() {
        String dominio = InputHelper.getString("Ingrese el dominio para listar usuarios: ");
        for (Usuario usuario : usuarios) {
            if (usuario.correo.endsWith("@" + dominio)) {
                imprimirUsuario(usuario);
            }
        }
    }

    private void imprimirUsuario(Usuario usuario) {
        String edadExacta = calcularEdadExacta(usuario.fechaNacimiento);
        System.out.println("Nombre: " + usuario.nombre + " " + usuario.apellido);
        System.out.println("Edad: " + edadExacta);
        System.out.println("Correo electrónico: " + usuario.correo);
        System.out.println("-----------------------------");
    }

    private boolean validarFechaNacimiento(String fechaNacimiento) {
        LocalDate nacimiento = LocalDate.parse(fechaNacimiento);
        LocalDate hoy = LocalDate.now();
        Period periodo = Period.between(nacimiento, hoy);
        return periodo.getYears() >= 13;
    }

    private boolean validarCorreo(String correo) {
        // Expresión regular para validar el correo electrónico
        String regexCorreo = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regexCorreo);
        Matcher matcher = pattern.matcher(correo);

        if (!matcher.matches()) {
            return false;
        }

        String dominio = correo.split("@")[1].toLowerCase();
        for (Usuario usuario : usuarios) {
            if (usuario.correo.split("@")[1].toLowerCase().equals(dominio)) {
                return false;  // Ya existe un usuario con ese correo y dominio
            }
        }

        return true;
    }

    private boolean validarContraseña(String contraseña) {
        // Expresión regular para validar la contraseña
        String regexContraseña = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?$%<>])[A-Za-z\\d!?$%<>]{8,}$";
        return contraseña.matches(regexContraseña);
    }

    private String calcularEdadExacta(String fechaNacimiento) {
        LocalDate nacimiento = LocalDate.parse(fechaNacimiento);
        LocalDate hoy = LocalDate.now();
        Period periodo = Period.between(nacimiento, hoy);

        int años = periodo.getYears();
        int meses = periodo.getMonths();
        int dias = periodo.getDays();

        return años + " años, " + meses + " meses, " + dias + " días";
    }
}

class InputHelper {
    public static int getInt(String prompt) {
        System.out.print(prompt);
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        return scanner.nextInt();
    }

    public static String getString(String prompt) {
        System.out.print(prompt);
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        return scanner.nextLine();
    }
}
