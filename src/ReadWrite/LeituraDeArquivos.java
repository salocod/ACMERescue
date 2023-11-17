/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReadWrite;

import Atendimento.Atendimento;
import Equipe.Equipamentos.Tipos.Barco;
import Equipe.Equipamentos.Tipos.CaminhaoTanque;
import Equipe.Equipamentos.Tipos.Escavadeira;
import Equipe.Equipe;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import Manage.ACMERescue;
import Eventos.Tipos.Ciclone;
import Eventos.Tipos.Seca;
import Eventos.Tipos.Terremoto;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
/**
 *
 * @author nico
 */
public class LeituraDeArquivos {
    
    private ACMERescue acmeRescue;
    private String nomeArquivo;

    public LeituraDeArquivos(String arquivo, ACMERescue acmerescue) {
        this.acmeRescue = acmerescue;
        nomeArquivo=arquivo;
        readAll();
        acmeRescue.alocacaoAutomatica();
    }
    
    private void readAll() {
        readEvento();
        readAtendimento();
        readEquipe();
        readEquipamento();
    }
    
    private void readEvento() {
        File arquivo = new File("ReadWrite/LEITURA/NORMAL/" +nomeArquivo + "-EVENTOS.csv");
        
        if(!arquivo.exists()) return;
        try(BufferedReader br = new BufferedReader(new FileReader(arquivo))){
            String linha;
            br.readLine();
            while((linha = br.readLine()) != null) {
                try {
                Scanner sc = new Scanner(linha).useDelimiter("[;]");
                String codigo = sc.next();
                String data = sc.next();
                double latitude = sc.nextDouble();
                double longitude = sc.nextDouble();
                int tipo = sc.nextInt();
                double valor = sc.nextDouble();
                   switch(tipo) {
                       case 1 -> {
                           acmeRescue.adicionarEvento(new Ciclone(codigo, data, latitude, longitude, valor, sc.nextDouble()));
                       }
                       case 2 -> {
                           acmeRescue.adicionarEvento(new Terremoto(codigo, data, latitude, longitude, valor));
                       }
                       case 3 -> {
                           acmeRescue.adicionarEvento(new Seca(codigo, data, latitude, longitude, (int) valor));
                       }
                       default -> {break;}
                   }
                }catch(InputMismatchException exception) {
                    System.out.println("Dados invalidos");}
                
            }
        }catch(IOException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }
    
    private void readAtendimento() {
        File arquivo = new File("ReadWrite/LEITURA/NORMAL/" +nomeArquivo + "-ATENDIMENTOS.csv");
        
        if (!arquivo.exists())return;
        
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            br.readLine();
            while((linha = br.readLine()) != null) {
                try {
                Scanner sc = new Scanner(linha).useDelimiter("[;]");
                int codigo = sc.nextInt();
                String dataInicio = sc.next();
                int duracao = sc.nextInt();
                String status = sc.next();
                String codigoEvento = sc.next();
                
                if(!acmeRescue.adicionarAtendimento(new Atendimento(codigo, dataInicio, duracao, acmeRescue.pesquisarCodigoEvento(codigoEvento))))
                    System.out.println("Falha ao adicionar atendimento.");
                    } catch (InputMismatchException e) {System.out.println("Dados invalidos");}
            }   
        } catch(Exception e) {}
    }
    
    private void readEquipamento() {
        File arquivo = new File("ReadWrite/LEITURA/NORMAL/" +nomeArquivo + "-EQUIPAMENTOS.csv");
        
        if (!arquivo.exists())return;
        
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            br.readLine();
            while((linha = br.readLine()) != null) {
                try {
                Scanner sc = new Scanner(linha).useDelimiter("[;]");
                int id = sc.nextInt();
                String nome = sc.next();
                double custodiario = sc.nextDouble();
                Equipe equipe = acmeRescue.pesquisarCodigoEquipe(sc.next());
                if(equipe == null) continue;
                int tipo = sc.nextInt();
                switch(tipo) {
                    case 1 -> {
                        acmeRescue.adicionarEquipamento(new Barco(id, nome, custodiario, sc.nextInt(), equipe));
                        }
                    case 2 -> {
                        acmeRescue.adicionarEquipamento(new CaminhaoTanque(id, nome, custodiario, sc.nextDouble(), equipe));
                    }
                    case 3 -> {
                        acmeRescue.adicionarEquipamento(new Escavadeira(id, nome, custodiario, sc.next(), sc.nextDouble(), equipe));
                    }
                }
                } catch(InputMismatchException e) {
                    System.out.println("Dados invalidos");
                }
            } 
        } catch(Exception e) {}
        
    }
    
    private void readEquipe() {
        File arquivo = new File("ReadWrite/LEITURA/NORMAL/" +nomeArquivo + "-EQUIPES.csv");
        
        if (!arquivo.exists())return;
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            br.readLine();
            while((linha = br.readLine()) != null) {
                Scanner sc = new Scanner(linha).useDelimiter("[;]");
                try {
                acmeRescue.adicionarEquipe(new Equipe(sc.next(), sc.nextInt(), sc.nextDouble(), sc.nextDouble()));
                }catch(InputMismatchException e) {}
            }
        } catch(Exception exception) {}
    }
    
}
