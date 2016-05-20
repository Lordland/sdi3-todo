using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApplication1
{
    class Program
    {

        static void Main(String[] args)
        {
            run();
        }

        private static String mensaje = "Opciones: \n\t1.Listar usuarios y viajes "
            + "promovidos y apuntados\n\t2.Excluir usuario\n\t3.Listar "
            + "comentarios y puntuaciones\n\t4.Eliminar comentarios y "
            + "puntuaciones\n\t0.Salir";

        private static void run()
        {
		while(true){
                Console.WriteLine(mensaje);
                Console.WriteLine("Diga su opcion: ");
                int opcion = int.Parse(Console.ReadLine());
                if (opcion == 1)
                {
                    mostrarDatos();
                }
                else if (opcion == 2)
                {
                    cancelarUsuario();
                }
                else if (opcion == 3)
                {//TODO no estan ordenados de mas reciente a menos
                    listar();
                }
                else if (opcion == 4)
                {
                    borrarRatings();
                }
                else if (opcion == 0)
                {
                    return;
                }
                else
                {
                    Console.WriteLine("Error, vuelva a mirar las opciones "
                            + "disponibles");
                }
            }
        }


        private static void listar()
        {
            EJBTripServiceService serviceT = new EJBTripServiceService();
            trip[] trips = serviceT.listarViajes();
            List<trip> tr = new List<trip>();
            DateTime d = new DateTime();
            //d.setTime(d.getTime() + 30 * 1000 * 60 * 60 * 24);
		    foreach(trip t in trips){
                //if (t.departureDate.CompareTo < 0)
                //{
                    tr.Add(t);
               // }
            }
		    foreach(trip t in tr){
               Console.WriteLine("Viaje: " + t.id + " salida: " +
                        t.departure.city + " destino: "
                        + t.destination.city);
                EJBRatingServiceService service = new EJBRatingServiceService();
                rating[] ratings = service.listarComentarios(t);
                if (ratings != null)
                {
                    foreach (rating r in ratings)
                    {
                        user u1 = new EJBUserServiceService()
                                .findById(r.seatFromUserId);
                        user u2 = new EJBUserServiceService()
                                .findById(r.seatAboutUserId);
                        Console.WriteLine("\t" + t.destination + " " +
                                u1.name + " " + u2.name + " " + r.value
                                + "\n\t" + r.comment);
                    }
                }
                else
                {
                    Console.WriteLine("No hay comentarios");
                }
            }
        }



        private static rating[] listaRatings()
        {
            EJBRatingServiceService service = new EJBRatingServiceService();
            rating[] ratings = service.listarRatings();
            if (ratings != null)
            {
                foreach (rating r in ratings)
                {
                    Console.WriteLine(r.id + " " + r.seatFromTripId +
                            " " + r.seatFromUserId);
                }
            }
            return ratings;
        }

        private static void borrarRatings()
        {
            while (true)
            {
                Console.WriteLine("Lista de ratings: ");
                rating[] r = listaRatings();
                Console.WriteLine("Seleccione el id del rating "
                        + "que desee borrar o 0 para salir");
                long id = long.Parse(Console.ReadLine());
                if (r != null)
                {
                    foreach (rating rat in r)
                    {
                        if (id != 0 && rat.id.Equals(id))
                        {
                            new EJBRatingServiceService()
                            .eliminarComentarios(rat.id, true);
                            Console.WriteLine("Rating borrado");
                            return;
                        }
                    }
                }
                if (id == 0L)
                {
                    return;
                }
                Console.WriteLine("Error, por favor introduzca un id de la lista");
            }
        }

        private static void cancelarUsuario()
        {
            while (true)
            {
                Console.WriteLine("Usuarios disponibles: ");
                mostrarUsuarios();
                Console.WriteLine("Seleccione el id del usuario o 0 para salir: ");
                long idUsuario = long.Parse(Console.ReadLine());
                if (existeUsuario(idUsuario))
                {
                    EJBUserServiceService userS = new EJBUserServiceService();
                    user u = userS.findById(idUsuario);
                    u.status = userStatus.CANCELLED;
                    EJBApplicationServiceService service = new EJBApplicationServiceService();
                    listaApuntados[] user = service.listaApuntadosUsuario(u);
                    if (user != null) { 
                        foreach (listaApuntados l in user)
                        {
                            service.cancelarUsuario(l);
                            Console.WriteLine("El usuario se ha cancelado con exito");
                        }
                    }
                    userS.updateUser(u);
                    return;
                }
                if (idUsuario == 0L)
                {
                    return;
                }
                else
                {
                    Console.WriteLine("El usuario no existe, "
                            + "elija uno de la lista");
                }
            }
        }

        private static bool existeUsuario(long id)
        {
            user[] usuarios = getUsuarios();
            foreach (user u in usuarios)
            {
                if (u.id == id)
                {
                    return true;
                }
            }
            return false;
        }

        private static int listaViajePromotor(long id)
        {
            trip[] viajes = new EJBTripServiceService().listarViajes();
            List<trip> aux = new List<trip>();
            foreach (trip t in viajes)
            {
                if (t.promoterId.Equals(id))
                {
                    aux.Add(t);
                }
            }
            return aux.Count;
        }

        private static void mostrarUsuarios()
        {
            user[] u = getUsuarios();
            foreach (user us in u)
            {
               Console.WriteLine("Id: " + us.id + " nombre: "
                        + us.name + " apellido" + us.surname);
            }
        }

        private static int listaViajeUsuario(user u)
        {
            EJBApplicationServiceService service = new EJBApplicationServiceService();
            listaApuntados[] user = service.listaApuntadosUsuario(u);
            if(user == null)
            {
                return 0;
            }
            return user.Length;
        }

        private static user[] getUsuarios()
        {
            EJBUserServiceService service = new EJBUserServiceService();
            return service.getUsers();
        }

        private static void mostrarDatos()
        {
            user[] user = getUsuarios();
            foreach (user u in user)
            {
                int listaP = listaViajePromotor(u.id);
                int listaA = listaViajeUsuario(u);
                Console.WriteLine("Usuario: " + u.name + " " + u.surname
                        + " " + u.email);
                Console.WriteLine("\tPromovio: " + listaP + " viajes");
                Console.WriteLine("\tSe apunto a: " + listaA + " viajes");
            }
        }


        
    }
}
