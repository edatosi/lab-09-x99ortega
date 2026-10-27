package listas;

import java.util.NoSuchElementException;
import java.util.Objects;
/**
 * Lista simplemente enlazada genérica.
 *
 * INSTRUCCIONES PARA EL ESTUDIANTE
 * --------------------------------
 * 1. Cada método marcado con "TODO" lanza UnsupportedOperationException.
 *    Reemplace esa línea por su implementación.
 * 2. NO modifique las pruebas (src/test). 
 * 3. Resuelva en orden (Etapa 1, 2, 3...): cada etapa usa las anteriores.
 * 4. Solo se permite usar nodos propios (clase Nodo). No use
 *    java.util.LinkedList ni ArrayList dentro de esta clase.
 */
public class ListaEnlazada<T> {

    /** Primer nodo de la lista (null cuando la lista está vacía). */
    private Nodo<T> cabeza;

    /** Cantidad de elementos. Debe mantenerse actualizada en cada operación. */
    private int cantidad;

    public ListaEnlazada() {
        this.cabeza = null;
        this.cantidad = 0;
    }

    // =====================================================================
    // ETAPA 1. Estado de la lista
    // =====================================================================

   /** @return true si la lista no tiene elementos. */
    public boolean estaVacia() {
        // La lista está vacía cuando no hay ningún nodo al inicio.
        return cabeza == null;
    }

    /** @return número de elementos de la lista. */
    public int tamanio() {
        return cantidad;
    }

    // =====================================================================
    // ETAPA 2. Agregar en los extremos
    // =====================================================================

     /** Inserta el dato como nuevo primer elemento. */
    public void agregarAlInicio(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        // El nuevo nodo apunta a la cabeza actual y pasa a ser la nueva cabeza.
        nuevoNodo.setSiguiente(cabeza);
        cabeza = nuevoNodo;
        cantidad++;
    }


   /** Inserta el dato como nuevo último elemento. */
    public void agregarAlFinal(T dato) {
        // En una lista vacía, el nuevo elemento es a la vez el primero.
        if (estaVacia()) {
            agregarAlInicio(dato);
            return;
        }

        // Se recorre la lista hasta el último nodo (el que no tiene siguiente).
        Nodo<T> ultimoNodo = cabeza;
        while (ultimoNodo.getSiguiente() != null) {
            ultimoNodo = ultimoNodo.getSiguiente();
        }

        ultimoNodo.setSiguiente(new Nodo<>(dato));
        cantidad++;
    }
    // =====================================================================
    // ETAPA 3. Consultar elementos
    // =====================================================================

       /** @throws NoSuchElementException si la lista está vacía */
    public T obtenerPrimero() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }
        return cabeza.getDato();
    }

    /** @throws NoSuchElementException si la lista está vacía */
    public T obtenerUltimo() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }

        Nodo<T> ultimoNodo = cabeza;
        while (ultimoNodo.getSiguiente() != null) {
            ultimoNodo = ultimoNodo.getSiguiente();
        }
        return ultimoNodo.getDato();
    }

    /**
     * @param indice posición (0 = primer elemento)
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T obtener(int indice) {
        return obtenerNodo(indice).getDato();
    }

    // =====================================================================
    // ETAPA 4. Búsqueda
    // =====================================================================

  /** @return true si algún elemento es igual (equals) al dato. Admite null. */
    public boolean contiene(T dato) {
        return indiceDe(dato) != -1;
    }

     /** @return posición de la primera aparición del dato, o -1 si no está. */
    public int indiceDe(T dato) {
        Nodo<T> nodoActual = cabeza;
        int posicion = 0;

        while (nodoActual != null) {
            // Objects.equals compara por contenido y además tolera valores null.
            if (Objects.equals(nodoActual.getDato(), dato)) {
                return posicion;
            }
            nodoActual = nodoActual.getSiguiente();
            posicion++;
        }

        return -1;
    }

    // =====================================================================
    // ETAPA 5. Insertar y reemplazar en cualquier posición
    // =====================================================================

      /**
     * Inserta el dato en la posición indicada, desplazando los siguientes.
     * Se permite indice == tamanio() (equivale a agregar al final).
     *
     * @throws IndexOutOfBoundsException si indice < 0 o indice > tamanio()
     */
    public void insertar(int indice, T dato) {
        // Aquí sí se admite indice == cantidad, porque insertar al final es válido.
        if (indice < 0 || indice > cantidad) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        if (indice == 0) {
            agregarAlInicio(dato);
            return;
        }

        // Hay que detenerse en el nodo ANTERIOR a la posición pedida.
        Nodo<T> nodoAnterior = obtenerNodo(indice - 1);
        Nodo<T> nuevoNodo = new Nodo<>(dato);

        nuevoNodo.setSiguiente(nodoAnterior.getSiguiente());
        nodoAnterior.setSiguiente(nuevoNodo);
        cantidad++;
    }

     /**
     * Reemplaza el dato de la posición indicada.
     *
     * @return el dato que había antes
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T reemplazar(int indice, T dato) {
        Nodo<T> nodo = obtenerNodo(indice);

        // Se guarda el valor anterior antes de sobrescribirlo, para poder retornarlo.
        T datoAnterior = nodo.getDato();
        nodo.setDato(dato);
        return datoAnterior;
    }
    // =====================================================================
    // ETAPA 6. Eliminar
    // =====================================================================

    /**
     * Elimina y retorna el primer elemento.
     * @throws NoSuchElementException si la lista está vacía
     */
    public T eliminarPrimero() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }

        T datoEliminado = cabeza.getDato();
        // La cabeza pasa a ser el segundo nodo; el primero queda descartado.
        cabeza = cabeza.getSiguiente();
        cantidad--;
        return datoEliminado;
    }
    /**
     * Elimina y retorna el último elemento.
     * @throws NoSuchElementException si la lista está vacía
     */
    public T eliminarUltimo() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }

        // Con un solo elemento, el último es también el primero.
        if (cantidad == 1) {
            return eliminarPrimero();
        }

        // Se recorre hasta el penúltimo nodo para poder cortar el enlace final.
        Nodo<T> nodoPenultimo = obtenerNodo(cantidad - 2);
        T datoEliminado = nodoPenultimo.getSiguiente().getDato();

        nodoPenultimo.setSiguiente(null);
        cantidad--;
        return datoEliminado;
    }
      /**
     * Elimina y retorna el elemento de la posición indicada.
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T eliminar(int indice) {
        validarIndice(indice);

        if (indice == 0) {
            return eliminarPrimero();
        }

        // El nodo anterior se enlaza directamente con el siguiente del eliminado.
        Nodo<T> nodoAnterior = obtenerNodo(indice - 1);
        Nodo<T> nodoEliminado = nodoAnterior.getSiguiente();

        nodoAnterior.setSiguiente(nodoEliminado.getSiguiente());
        cantidad--;
        return nodoEliminado.getDato();
    }
       /**
     * Elimina la primera aparición del dato.
     * @return true si se eliminó algo, false si el dato no estaba
     */
    public boolean eliminarValor(T dato) {
        int posicion = indiceDe(dato);

        if (posicion == -1) {
            return false;
        }

        eliminar(posicion);
        return true;
    }

    /** Deja la lista vacía. */
    public void vaciar() {
        // Al soltar la cabeza, el resto de nodos queda sin referencias.
        cabeza = null;
        cantidad = 0;
    }

    // =====================================================================
    // ETAPA 7. Representación en texto
    // =====================================================================

      /**
     * Formato: "[1, 2, 3]". Lista vacía: "[]".
     * Un elemento null se imprime como "null".
     */
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder("[");
        Nodo<T> nodoActual = cabeza;

        while (nodoActual != null) {
            texto.append(nodoActual.getDato());
            // La coma solo se agrega si todavía falta algún elemento.
            if (nodoActual.getSiguiente() != null) {
                texto.append(", ");
            }
            nodoActual = nodoActual.getSiguiente();
        }

        return texto.append("]").toString();
    }

    // =====================================================================
    // ETAPA 8. Retos
    // =====================================================================

      /**
     * Invierte la lista EN SITIO: no cree nodos nuevos, solo cambie
     * las referencias 'siguiente' de los nodos existentes.
     */
    public void invertir() {
        Nodo<T> nodoAnterior = null;
        Nodo<T> nodoActual = cabeza;

        while (nodoActual != null) {
            // Se guarda el siguiente antes de romper el enlace hacia adelante.
            Nodo<T> nodoSiguiente = nodoActual.getSiguiente();

            nodoActual.setSiguiente(nodoAnterior);

            // Las tres referencias avanzan una posición.
            nodoAnterior = nodoActual;
            nodoActual = nodoSiguiente;
        }

        // Al terminar, 'nodoAnterior' quedó en el que era el último nodo.
        cabeza = nodoAnterior;
    }
      /**
     * Retorna el elemento del medio. Si el tamaño es par, el segundo de los
     * dos centrales (ej.: [1,2,3,4] -> 3). Pista: dos punteros, uno avanza
     * de a 1 y otro de a 2.
     *
     * @throws NoSuchElementException si la lista está vacía
     */
    public T obtenerMedio() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }

        Nodo<T> nodoLento = cabeza;
        Nodo<T> nodoRapido = cabeza;

        // El rápido avanza de a dos: cuando llega al final, el lento va por la mitad.
        while (nodoRapido != null && nodoRapido.getSiguiente() != null) {
            nodoLento = nodoLento.getSiguiente();
            nodoRapido = nodoRapido.getSiguiente().getSiguiente();
        }

        return nodoLento.getDato();
    }

      /**
     * Elimina los elementos repetidos dejando solo la primera aparición
     * de cada uno, conservando el orden. Ej.: [1,2,1,3,2] -> [1,2,3]
     */
    public void eliminarDuplicados() {
        Nodo<T> nodoActual = cabeza;

        // Por cada elemento se revisa el resto de la lista buscando repeticiones.
        while (nodoActual != null) {
            Nodo<T> nodoAnterior = nodoActual;
            Nodo<T> nodoRevisado = nodoActual.getSiguiente();

            while (nodoRevisado != null) {
                if (Objects.equals(nodoRevisado.getDato(), nodoActual.getDato())) {
                    // Se salta el nodo repetido enlazando el anterior con el siguiente.
                    nodoAnterior.setSiguiente(nodoRevisado.getSiguiente());
                    cantidad--;
                } else {
                    nodoAnterior = nodoRevisado;
                }
                nodoRevisado = nodoRevisado.getSiguiente();
            }

            nodoActual = nodoActual.getSiguiente();
        }
    }

     // =====================================================================
    // Métodos auxiliares privados
    // =====================================================================

    /**
     * Valida que el índice corresponda a una posición existente.
     *
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    private void validarIndice(int indice) {
        if (indice < 0 || indice >= cantidad) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
    }

    /**
     * Recorre la lista desde la cabeza y retorna el nodo de la posición pedida.
     *
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    private Nodo<T> obtenerNodo(int indice) {
        validarIndice(indice);

        Nodo<T> nodoActual = cabeza;
        for (int posicion = 0; posicion < indice; posicion++) {
            nodoActual = nodoActual.getSiguiente();
        }
        return nodoActual;
    }
}
