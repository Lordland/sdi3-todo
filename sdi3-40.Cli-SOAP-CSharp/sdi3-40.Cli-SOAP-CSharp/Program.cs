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
                {
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
            trip[] trips = serviceT.listarViajesUltimoMes();
		    foreach(trip t in trips){
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
                if (idUsuario == 0L)
                {
                    return;
                }
                EJBUserServiceService userS = new EJBUserServiceService();
                bool borrado = userS.darDeBajaUsuario(idUsuario,true);
                if (!borrado)
                {
                        Console.WriteLine("El usuario no existe, "
                                + "elija uno de la lista");
                    }
                    else
                    {
                        Console.WriteLine("Usuario borrado satisfactoriamente");
                    }
                return;
                }
                
               
            }
        

    private static void mostrarUsuarios()
    {
        EJBUserServiceService service = new EJBUserServiceService();
        user[] u = service.getUsers();
        foreach (user us in u)
        {
            Console.WriteLine("Id: " + us.id + " nombre: " + us.name
                    + " apellido" + us.surname);
        }
    }

    

       

        private static void mostrarDatos()
        {
            EJBUserServiceService userS = new EJBUserServiceService();
            EJBTripServiceService serviceT = new EJBTripServiceService();
            EJBApplicationServiceService serviceA = new EJBApplicationServiceService();
            user[] user = userS.getUsers();
            foreach (user u in user)
            {
                trip[] listaPromotor = serviceT.listaViajePromotor(u.id,false);
                int listaP;
                if(listaPromotor == null)
                {
                    listaP = 0;
                }else
                {
                    listaP = listaPromotor.Length;
                }
                listaApuntados[] listaApuntados = serviceA.listaApuntadosUsuario(u);
                int listaA;
                if (listaApuntados == null)
                {
                    listaA = 0;
                }
                else
                {
                    listaA = listaApuntados.Length;
                }
                Console.WriteLine("Usuario: " + u.name + " " + u.surname
                        + " " + u.email);
                Console.WriteLine("\tPromovio: " + listaP + " viajes");
                Console.WriteLine("\tSe apunto a: " + listaA + " viajes");
            }
        }


        
    }
}
