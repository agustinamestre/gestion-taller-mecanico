export type MedioContacto = 'EMAIL' | 'WHATSAPP';

export type MotivoAlerta = 'TIEMPO' | 'KILOMETRAJE';

export const MOTIVO_ALERTA_LABELS: Record<MotivoAlerta, string> = {
  TIEMPO: 'Por tiempo',
  KILOMETRAJE: 'Por kilometraje',
};

export function etiquetaMotivoAlerta(motivo: MotivoAlerta | null): string {
  return MOTIVO_ALERTA_LABELS[motivo ?? 'TIEMPO'];
}

export interface Alerta {
  id: number;
  vehiculoId: number;
  patenteVehiculo: string;
  nombreCliente: string;
  telefonoCliente: string | null;
  fechaAlerta: string;
  tipo: string;
  motivo: MotivoAlerta | null;
  contactado: boolean;
  fechaContacto: string | null;
  medioContacto: MedioContacto | null;
  observaciones: string | null;
}

export interface ContactarAlertaRequest {
  observaciones?: string;
}
