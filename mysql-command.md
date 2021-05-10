## Installation
sudo apt install mysql-server

## Démarrer le serveur MySQL
sudo systemctl start mysql

## Redemarrer le serveur MySQL
sudo systemctl restart mysql

## Arreter le serveur MySQL
sudo systemctl stop mysql

## Lancer MySQL
sudo mysql

## Executer le script fichier.sql
source fichier.sql;

## Creer une bdd
create database databasename;

## Supprimer une bdd
drop database databasename;

## Afficher toutes les bdd
show databases;

## Selectionner une bdd
use databasename;

## Afficher toutes les tables d'une bdd
show tables;

## Supprimer une table d'une bdd
drop table tablename;

## Afficher les types des champs d'une bdd
describe tablename;