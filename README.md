# PostCond

![alt text](schema.png)


## **Objectif 1 — Solution directe**

| **Opération**        | **Pré-action**                              | **Garde (condition)**        	       | **Post-action**                                          |
|------------------------|-------------------------------------------|-----------------------------------------|----------------------------------------------------------|
| **Produce(Message m)** | Insertion d’un message par un *Producer*  | `nfull < bufSize` *(buffer non plein)*  | `nempty--` ; `nfull++` ;  |
| **Consume()**          | Retrait d’un message par un *Consumer*    | `nfull > 0` *(buffer non vide)*         | `nfull--` ; `nempty++` ;      |

---

## **Objectif 2 — Terminaison**

Extension de l'objectif 1 pour permettre la terminaison des producteurs et consommateurs.

---

## **Objectif 3 — Sémaphores**

---

## **Objectif 4 — Moniteurs (avec ReentrantLock et Condition)**

---

## **Objectif 5 — Multi-consommation**

---

## **Objectif 6 — Multi-exemplaires synchrone**