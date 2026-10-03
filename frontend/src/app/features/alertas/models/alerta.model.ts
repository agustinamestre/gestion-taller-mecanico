export type MedioContacto = 'EMAIL' | 'WHATSAPP';

export interface Alerta {
  id: number;
  vehiculoId: number;
  patenteVehiculo: string;
  nombreCliente: string;
  telefonoCliente: string | null;
  fechaAlerta: string;
  tipo: string;
  contactado: boolean;
  fechaContacto: string | null;
  medioContacto: MedioContacto | null;
  observaciones: string | null;
}

export interface ContactarAlertaRequest {
  observaciones?: string;
}
