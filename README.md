# Hugbunadarverkefni1

# Basic

Sjá stöðuna á verkefni => ´git status´

Sjá branches og í hverju þú ert í => ´git branch´

# Skrá username og password (svo þið þurfið ekki að gera það fyrir hvert push)

git config --global user.name "notandanafn"

git config --global user.email "email@email.com"

(það er líka hægt að láta git muna psswordið þitt)

# Byrja að nota Git og sækja verkefnið

Gera nýja möppu

git remote

git clone https://github.com/alli959/Hugbunadarverkefni1


Núna ætti ´git remote´ að sýna "origin" á skjánum.

´git fetch origin master´ sækir öll ný skjöl sem er búið að bæta við projectið en merge-ar ekki.

´git pull origin master´ sækir öll ný skjöl og uppfærir þau sem þú ert með.

# Dæmi með git branch og fara i tad

git checkout branchnafn

# Gera prufu skrá og commit-a

touch test.txt

git add -A

git commit -m "Prufuskrá bætt við"

Núna er búið að bæta við og commit-a test.txt á local branchinu, ekki master.

# Hvernig skal vinna þetta.

git branch "þá sérðu hvaða branchi þú ert í, vertu í þínu"

git add skjalnafn

git commit -m "message"

git push origin branchnafn

git checkout master

git pull origin master  "mikilvægt að athuga hvort þú getir pullað, áður en þú pushar"

git merge branchnafn

git push origin master

# Eyða branch ef það verður ekki notað aftur

git branch -d nafn-a-branchi

git push origin --delete nafn-a-branchi
