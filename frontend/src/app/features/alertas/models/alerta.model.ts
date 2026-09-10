export type MedioContacto = 'EMAIL' | 'WHATSAPP';

export interface Alerta {
  id: number;
  vehiculoId: number;
  patenteVehiculo: string;
  nombreCliente: string;
  fechaAlerta: string;
  tipo: string;
  contactado: boolean;
  fechaContacto: string | null;
  medioContacto: MedioContacto | null;
  observaciones: string | null;
}

export interface ContactarAlertaRequest {
  medioContacto: MedioContacto;
  observaciones?: string;
}
