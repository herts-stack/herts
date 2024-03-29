// Don't edit this file because this file is generated by herts codegen.
import {NestedCustomModel, RequestHeaders} from './herts-structure.gen'
import {IntegerFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {DoubleClassFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {ByteClassFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {ShortClassFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {LongClassFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {FloatClassFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {BooleanClassFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {BooleanFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {CharacterFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {BigDecimalFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {ListStrFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {CustomModel} from './herts-structure.gen'
import {CustomModelFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {ArrayListFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {CustomModelListFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {HashMapFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {CustomModelMapFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {StringFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {IntFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {DoubleFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {ByteFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {ShortFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {LingFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {FloatFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {CharFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {BigIntFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {DateFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {UuidFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {MapStrFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {VoidFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {ArrayFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {ListFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {MapFuncMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {User} from './herts-structure.gen'
import {CreateUserMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {GetUsersMethodRequest} from './herts-HttpCodegenTestService-request-model.gen'
import {HttpCodegenTestServiceClient} from './herts-HttpCodegenTestService-client.gen'

async function main() {
    const header: RequestHeaders = {'Content-Type': 'application/json'};

    const clientHttpCodegenTestServiceClient = new HttpCodegenTestServiceClient ('http://localhost:8080');

    const req1 = IntegerFuncMethodRequest.createRequest (  );
    const res1 = await clientHttpCodegenTestServiceClient.integerFunc (header);
    console.log(res1);

    const req2 = DoubleClassFuncMethodRequest.createRequest (  );
    const res2 = await clientHttpCodegenTestServiceClient.doubleClassFunc (header);
    console.log(res2);

    const req3 = ByteClassFuncMethodRequest.createRequest (  );
    const res3 = await clientHttpCodegenTestServiceClient.byteClassFunc (header);
    console.log(res3);

    const req4 = ShortClassFuncMethodRequest.createRequest (  );
    const res4 = await clientHttpCodegenTestServiceClient.shortClassFunc (header);
    console.log(res4);

    const req5 = LongClassFuncMethodRequest.createRequest (  );
    const res5 = await clientHttpCodegenTestServiceClient.longClassFunc (header);
    console.log(res5);

    const req6 = FloatClassFuncMethodRequest.createRequest (  );
    const res6 = await clientHttpCodegenTestServiceClient.floatClassFunc (header);
    console.log(res6);

    const req7 = BooleanClassFuncMethodRequest.createRequest (  );
    const res7 = await clientHttpCodegenTestServiceClient.booleanClassFunc (header);
    console.log(res7);

    const req8 = BooleanFuncMethodRequest.createRequest (  );
    const res8 = await clientHttpCodegenTestServiceClient.booleanFunc (header);
    console.log(res8);

    const req9 = CharacterFuncMethodRequest.createRequest (  );
    const res9 = await clientHttpCodegenTestServiceClient.characterFunc (header);
    console.log(res9);

    const req10 = BigDecimalFuncMethodRequest.createRequest (  );
    const res10 = await clientHttpCodegenTestServiceClient.bigDecimalFunc (header );
    console.log(res10);

    const req11 = ListStrFuncMethodRequest.createRequest (  );
    const res11 = await clientHttpCodegenTestServiceClient.listStrFunc (header );
    console.log(res11);

    const nModel = new NestedCustomModel(1, 'Hello');
    const cModel = new CustomModel(1, 'Data', nModel);
    const req12 = CustomModelFuncMethodRequest.createRequest ( cModel);
    const res12 = await clientHttpCodegenTestServiceClient.customModelFunc (header, req12 );
    console.log(res12);

    const req13 = ArrayListFuncMethodRequest.createRequest (  );
    const res13 = await clientHttpCodegenTestServiceClient.arrayListFunc (header );
    console.log(res13);

    const req14 = CustomModelListFuncMethodRequest.createRequest (  );
    const res14 = await clientHttpCodegenTestServiceClient.customModelListFunc (header);
    console.log(res14);

    const req15 = HashMapFuncMethodRequest.createRequest (  );
    const res15 = await clientHttpCodegenTestServiceClient.hashMapFunc (header );
    console.log(res15);

    const req16 = CustomModelMapFuncMethodRequest.createRequest (  );
    const res16 = await clientHttpCodegenTestServiceClient.customModelMapFunc (header );
    console.log(res16);

    const req17 = StringFuncMethodRequest.createRequest (  );
    const res17 = await clientHttpCodegenTestServiceClient.stringFunc (header);
    console.log(res17);

    const req18 = IntFuncMethodRequest.createRequest ( 1, '' );
    const res18 = await clientHttpCodegenTestServiceClient.intFunc (header, req18 );
    console.log(res18);

    const req19 = DoubleFuncMethodRequest.createRequest (  );
    const res19 = await clientHttpCodegenTestServiceClient.doubleFunc (header);
    console.log(res19);

    const req20 = ByteFuncMethodRequest.createRequest (  );
    const res20 = await clientHttpCodegenTestServiceClient.byteFunc (header);
    console.log(res20);

    const req21 = ShortFuncMethodRequest.createRequest (  );
    const res21 = await clientHttpCodegenTestServiceClient.shortFunc (header);
    console.log(res21);

    const req22 = LingFuncMethodRequest.createRequest (  );
    const res22 = await clientHttpCodegenTestServiceClient.lingFunc (header);
    console.log(res22);

    const req23 = FloatFuncMethodRequest.createRequest (  );
    const res23 = await clientHttpCodegenTestServiceClient.floatFunc (header );
    console.log(res23);

    const req24 = CharFuncMethodRequest.createRequest (  );
    const res24 = await clientHttpCodegenTestServiceClient.charFunc (header);
    console.log(res24);

    const req25 = BigIntFuncMethodRequest.createRequest (  );
    const res25 = await clientHttpCodegenTestServiceClient.bigIntFunc (header );
    console.log(res25);

    const req26 = DateFuncMethodRequest.createRequest (  );
    const res26 = await clientHttpCodegenTestServiceClient.dateFunc (header);
    console.log(res26);

    const req27 = UuidFuncMethodRequest.createRequest (  );
    const res27 = await clientHttpCodegenTestServiceClient.uuidFunc (header);
    console.log(res27);

    const req28 = MapStrFuncMethodRequest.createRequest (  );
    const res28 = await clientHttpCodegenTestServiceClient.mapStrFunc (header );
    console.log(res28);

    const req29 = VoidFuncMethodRequest.createRequest (  );
    const res29 = await clientHttpCodegenTestServiceClient.voidFunc (header);
    console.log(res29);

    const bArray = new Array<string>();
    bArray.push('hey')
    const req30 = ArrayFuncMethodRequest.createRequest ( bArray);
    const res30 = await clientHttpCodegenTestServiceClient.arrayFunc (header, req30 );
    console.log(res30);

    const req31 = ListFuncMethodRequest.createRequest (  );
    const res31 = await clientHttpCodegenTestServiceClient.listFunc (header);
    console.log(res31);

    const req32 = MapFuncMethodRequest.createRequest (  );
    const res32 = await clientHttpCodegenTestServiceClient.mapFunc (header);
    console.log(res32);

    const u = new User('1', 'name', new Date());
    const req33 = CreateUserMethodRequest.createRequest ( u );
    const res33 = await clientHttpCodegenTestServiceClient.createUser (header, req33 );
    console.log(res33);

    const req34 = GetUsersMethodRequest.createRequest (  );
    const res34 = await clientHttpCodegenTestServiceClient.getUsers (header);
    console.log(res34);

}
main();
