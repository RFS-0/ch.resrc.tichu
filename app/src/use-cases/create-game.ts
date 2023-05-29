export interface IntendedGame {
    id: string;
    createdBy: string;
    joinCode: string;
}

export interface CreateGame {
    send: (intent: any) => Promise<any>;
}
