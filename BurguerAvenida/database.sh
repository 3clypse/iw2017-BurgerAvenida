#!/bin/bash
# -*- coding: utf-8 -*-

read -p "Usuario con permisos en la base de datos:" usuario
mysql -u $usuario -p < iw2017-BurgerAvenida.sql

