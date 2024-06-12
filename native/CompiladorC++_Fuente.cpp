#include <iostream>
#include <fstream>
#include <jni.h>
#include <cstdlib>
#include "Acciones.h"
using namespace std;

JNIEXPORT void JNICALL Java_org_example_Acciones_CompilarYEjecutar(JNIEnv *env, jclass, jstring RutaArchivo) {
    const char *path = env->GetStringUTFChars(RutaArchivo, NULL);

    // La libreria toma la ruta del archivo temporal que se genera al usar
    // Todos los compiladores en formato .txt, este ya debe estar traducido en c++
    // Crea el archivo cpp en la carpeta del proyecto y despues lo ejecuta
    // Puedes ver el archivo .cpp generado al lado del pom.xml, es posible que para
    // Hacer modificaciones me tengan que decir porque se hizo en una version antigua
    // Del proyecto, cualquier pregunta Me la hacen a mi, 
    // att. Nestor 
    ifstream cppFile(path);
    if (!cppFile) {
        cerr<<"No se pudo abrir el archivo: "<<path<<endl;
        return;
    }

    string cppCode((istreambuf_iterator<char>(cppFile)), istreambuf_iterator<char>());
    cppFile.close();
    ofstream outputFile("temp.cpp");
    outputFile<<cppCode;
    outputFile.close();

    if (system("g++ temp.cpp -o temp.out") != 0) {
        cerr<<"Error al compilar el archivo"<<endl;
        return;
    }

    #ifdef _WIN32
        if (system("temp.out") != 0) {
            cerr<<"Error al ejecutar el archivo compilado"<<endl;
        }
        #else
        if (system("./temp.out") != 0) {
            cerr << "Error al ejecutar el archivo compilado"<<endl;
        }
    #endif
    env->ReleaseStringUTFChars(RutaArchivo, path);
}
