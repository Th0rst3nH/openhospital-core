ALTER TABLE OH_PATIENTHISTORY
CHANGE COLUMN `PAH_PHY_ALCOOL` `PAH_PHY_ALCOHOL` TINYINT(1) NULL DEFAULT 0 ;

ALTER TABLE OH_PATIENTHISTORY
CHANGE COLUMN `PAH_PHY_ALVO_NOR` `PAH_PHY_BOWEL_NOR` TINYINT(1) NULL DEFAULT 1 ,
CHANGE COLUMN `PAH_PHY_ALVO_ABN` `PAH_PHY_BOWEL_ABN` VARCHAR(30) NULL DEFAULT NULL ;
